package it.polito.tdp.extflightdelays.model;

public class Tratta implements Comparable<Tratta> {

	private Airport a1;
	private Airport a2;
	private double weight;

	public Tratta(Airport a1, Airport a2, double weight) {
		this.a1 = a1;
		this.a2 = a2;
		this.weight = weight;
	}

	public Airport getA1() {
		return a1;
	}

	public void setA1(Airport a1) {
		this.a1 = a1;
	}

	public Airport getA2() {
		return a2;
	}

	public void setA2(Airport a2) {
		this.a2 = a2;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Durata media tra ");
		builder.append(a1.getAirportName());
		builder.append(" e ");
		builder.append(a2.getAirportName());
		builder.append(" è: ");
		builder.append(weight);
		return builder.toString();
	}

	@Override
	public int compareTo(Tratta o) {
		return Double.compare(weight, o.getWeight());
	}

}
