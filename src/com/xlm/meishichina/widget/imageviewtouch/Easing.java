package com.xlm.meishichina.widget.imageviewtouch;

public interface Easing {

	double easeIn(double time, double start, double end, double duration);

	double easeInOut(double time, double start, double end, double duration);

	double easeOut(double time, double start, double end, double duration);
}
