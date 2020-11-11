package com.speedyteller;

import java.util.Random;
import java.util.concurrent.Callable;

public class BuilderOrderNumber implements Callable<Integer> {

	@Override
	public Integer call() throws Exception {
      return new Random().nextInt(100);
	}
}
