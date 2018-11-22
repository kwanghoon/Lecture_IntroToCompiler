package org.swlab.lib.parser.examples.rpc;

public class NumValue extends Value {
	private double value;

	public NumValue(double value) {
		super();
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value + "";
	}
	
}
