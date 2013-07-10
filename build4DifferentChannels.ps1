param($ProjectName, $ProjectRootDirectory, $KeystorePath, $StorePass, $KeyPass, $Alias)

$channels = @("360", "aimi8")
$defaultChannel = "androidmarket"
$jdkPath = "E:\Program Files (x86)\Java\jdk1.6.0_01"
$androidSDKPath = "F:\android-sdk4.1"
$signedPath = "$ProjectRootDirectory\bin\signed"
$zipalignedPath = "$ProjectRootDirectory\bin\zipaligned"
$sn = 1

Function BuildApk { 
    param ($ChannelName)
    
    ""    
    "--------------------"
    ""
    "$sn / $($channels.length + 1)"
    ""

    if ($ProjectName.length -gt 0) {
        #$ProjectName = $ProjectName.toLower()
        "ProjectName is: $ProjectName"
    } else {
        "ProjectName is: chinavalue.focus"
    }
    
    $ChannelName = $ChannelName.toLower()
    "ChannelName is: $ChannelName"    
    ""
    
    if (!(Test-Path -path $signedPath)) {
        New-Item $signedPath -type directory
    }
    
    if (!(Test-Path -path $zipalignedPath)) {
        New-Item $zipalignedPath -type directory
    }
    
    $manifestPath = "$ProjectRootDirectory\AndroidManifest.xml"
    $FILE = [System.IO.File]::ReadAllText($manifestPath)
    $FILE = [REGEX]::replace($FILE, '<meta-data\r?\n?\s+android:name="UMENG_CHANNEL"\r?\n?\s+(.*?)>', '<meta-data
            android:name="UMENG_CHANNEL"
            android:value="' + $ChannelName + '" />', [Text.regularexpressions.regexoptions]::SingleLine)
    $FILE = [REGEX]::replace($FILE, '<meta-data\r?\n?\s+android:name="WAPS_PID"\r?\n?\s+(.*?)>', '<meta-data
            android:name="WAPS_PID"
            android:value="' + $ChannelName + '" />', [Text.regularexpressions.regexoptions]::SingleLine)
    [System.IO.File]::WriteAllText($manifestPath, $FILE) 
    "building $ProjectName..."
    & "$ProjectRootDirectory\build"
    ""
    "signing $signedPath\$ProjectName-$ChannelName.apk..."
    & "$jdkPath\bin\jarsigner" -keystore $KeystorePath -storepass $StorePass -keypass $KeyPass -signedjar "$signedPath\$ProjectName-$ChannelName.apk" -verbose "$ProjectRootDirectory\bin\$ProjectName-release-unsigned.apk" $Alias -digestalg SHA1 -sigalg MD5withRSA
    
    if (Test-Path -path "$zipalignedPath\$ProjectName-$ChannelName.apk") {
    	Remove-Item "$zipalignedPath\$ProjectName-$ChannelName.apk"
    }
    
    & "$androidSDKPath\tools\zipalign" -v 4 "$signedPath\$ProjectName-$ChannelName.apk" "$zipalignedPath\$ProjectName-$ChannelName.apk"
}

if ($ProjectName.length -gt 0 -and $ProjectRootDirectory -gt 0 -and $KeystorePath -gt 0 -and $StorePass -gt 0 -and $KeyPass -gt 0 -and $Alias) {
    foreach($channel in $channels) {
        BuildApk($channel)
        $sn++
    }

    #build with the default channel, avoid AndroidManifest.xml to be modified eventually
    BuildApk($defaultChannel)
} else {
    ""
    "Please input correct parameters: ProjectName ProjectRootDirectory KeystorePath StorePass KeyPass Alias"
}