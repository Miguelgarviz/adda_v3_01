package us.lsi.common;


import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import us.lsi.streams.Stream2;

public class Multiset<E>  {

	/**
	 * @param <E> Tipo de los elementos del Multiset
	 * @return Un Multiset vac�o
	 */
	public static <E> Multiset<E> empty() {
		return new Multiset<E>();
	}
	
	
	/**
	 * @param m Un multiset
	 * @return Una copia
	 */
	public static <E> Multiset<E> copy(Multiset<E> m) {
		return new Multiset<E>(m.elements);
	}

	/**
	 * @param <E> Tipo de los elementos del Multiset
	 * @param it Un iterable
	 * @return Un Multiset construido a partir del iterable
	 */
	public static <E> Multiset<E> of(Collection<E> it){
		return Stream2.toMultiSet(it.stream());
	}
	
	/**
	 * @param <E> Tipo de los elementos del Multiset
	 * @param m Un Map
	 * @return Un Multiset construido a partir del Map
	 */
	public static <E> Multiset<E> of(Map<E,Integer> m){
		return new Multiset<>(m);
	}
	
	/**
	 * @param <E> Tipo de los elementos del Multiset
	 * @param m Un Multiset
	 * @return Un Map construido a partir del Multiset
	 */
	public static <E> Map<E,Integer> asMap(Multiset<E> m){
		Map<E,Integer> r = new HashMap<>();
		m.elementSet().stream().forEach(x->r.put(x,m.count(x)));
		return r;
	}
	
	/**
	 * @param <E> Tipo de los elementos del Multiset
	 * @param entries Una secuencia de elementos
	 * @return Un Multiset construidom a partir de la secuencia de elementos
	 */
	public static <E> Multiset<E> of(@SuppressWarnings("unchecked") E... entries){
		Multiset<E> s = Multiset.empty();
		Arrays.asList(entries)
		.stream()
		.forEach(x->s.add(x)); 		
		return s;
	}

	
	private Map<E,Integer>  elements;

	private Multiset() {
		super();
		this.elements = new HashMap<>();
	}

	private Multiset(Map<E, Integer> elements) {
		super();
		this.elements = new HashMap<>(elements);
	}

	public void clear() {
		elements.clear();
	}

	public boolean containsKey(Object arg0) {
		return elements.containsKey(arg0);
	}

	public boolean containsValue(Object arg0) {
		return elements.containsValue(arg0);
	}

	public Set<E> elementSet() {
		return elements.keySet();
	}

	public boolean equals(Object arg0) {
		return elements.equals(arg0);
	}

	public Integer count(Object e) {
		Integer r = 0;
		if(this.elements.containsKey(e)) {
		     r = elements.get(e);
		}
		return r;
	}

	public int hashCode() {
		return elements.hashCode();
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}


	public Integer add(E e, Integer n) {
		Preconditions.checkArgument(n>=0,"No se pueden a�adir cantidades negativas");
		Integer r = n;
		if(r>0) elements.put(e,elements.getOrDefault(e,0) + n);
		return r;
	}

	public Integer add(E e) {
		Integer r = 1;
		if(this.elements.containsKey(e)) {
		     r = elements.get(e) + r;
		}
		return elements.put(e, r);
	}	
	
	public static <E> Multiset<E> add(Multiset<E> m1, Multiset<E> m2) {
		Set<E> st = Set2.union(m1.elementSet(),m2.elementSet());
		Multiset<E> r = Multiset.empty();
		st.stream().forEach(x->r.add(x,m1.count(x)+m2.count(x)));
		return r;
	}
	
	public Multiset<E> add(Multiset<E> m2) {
		return Multiset.add(this,m2);
	}
	
	public static <E> Multiset<E> difference(Multiset<E> m1, Multiset<E> m2) {
		Set<E> st = Set2.union(m1.elementSet(),m2.elementSet());
		Multiset<E> r = Multiset.empty();
		st.stream().forEach(x->r.add(x,m1.count(x)-m2.count(x)>=0?m1.count(x)-m2.count(x):0));
		return r;
	}
	
	public Multiset<E> difference(Multiset<E> m2) {
		return Multiset.difference(this,m2);
	}
	
	public static <E> Multiset<E> union(Multiset<E> m1, Multiset<E> m2) {
		Set<E> st = Set2.union(m1.elementSet(),m2.elementSet());
		Multiset<E> r = Multiset.empty();
		st.stream().forEach(x->r.add(x,Math.max(m1.count(x),m2.count(x))));
		return r;
	}
	
	public Multiset<E> union(Multiset<E> m2) {
		return Multiset.union(this,m2);
	}
	
	public static <E> Multiset<E> intersection(Multiset<E> m1, Multiset<E> m2) {
		Set<E> st = Set2.union(m1.elementSet(),m2.elementSet());
		Multiset<E> r = Multiset.empty();
		st.stream().forEach(x->r.add(x,Math.min(m1.count(x),m2.count(x))));
		return r;
	}
	
	public Multiset<E> intersection(Multiset<E> m2) {
		return Multiset.union(this,m2);
	}
	
	public static <E> Multiset<E> symmetricDifference(Multiset<E> m1, Multiset<E> m2) {
		Set<E> st = Set2.union(m1.elementSet(),m2.elementSet());
		Multiset<E> r = Multiset.empty();
		st.stream().forEach(x->r.add(x,Math.abs(m1.count(x)-m2.count(x))));
		return r;
	}
	
	public Multiset<E> symmetricDifference(Multiset<E> m2) {
		return Multiset.symmetricDifference(this,m2);
	}
	
	public static <E> Boolean isIncluded(Multiset<E> m1, Multiset<E> m2) {
		Set<E> st = Set2.union(m1.elementSet(),m2.elementSet());
		return st.stream().allMatch(x->m1.count(x) <= m2.count(x));
	}
	
	public Boolean isIncluded(Multiset<E> m2) {
		return Multiset.isIncluded(this,m2);
	}

	public Integer remove(Object e) {
		return elements.remove(e);
	}

	public List<Pair<E,Integer>> maxValues(Integer n) {
		List<Pair<E,Integer>> r = this.elements.keySet().stream()			
				.sorted(Comparator.comparing(k->this.elements.get(k)).reversed())
				.limit(n)
				.map(k->Pair.of(k,this.elements.get(k)))
				.toList();
		return r;
	}
	
	public int size() {
		return elements.size();
	}
	
	public int itemsNumber() {
		return elements.keySet().stream().mapToInt(e->this.count(e)).sum();
	}

	public String toString() {
		return elements
				.keySet()
				.stream()
				.filter(x->this.count(x)>0)
				.map(x->String.format("%s:%d",x,this.count(x)))
				.collect(Collectors.joining(",","{","}"));
	}

}
