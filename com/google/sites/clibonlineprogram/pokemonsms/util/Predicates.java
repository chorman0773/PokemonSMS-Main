package com.google.sites.clibonlineprogram.pokemonsms.util;

import java.util.function.Predicate;

public class Predicates {
private Predicates() {}


public static <T> Predicate<T> alwaysTrue(){
	return  t->true;
}
public static <T> Predicate<T> alwaysFalse(){
	return t->false;
}

public static <T> Predicate<T> or(final Predicate<? super T> a, final Predicate<? super T> b){
	return new Predicate<T>() {

		@Override
		public boolean test(T t) {
			// TODO Auto-generated method stub
			return a.test(t)||b.test(t);
		}

	};
}
@SafeVarargs
public static <T> Predicate<T> or(final Predicate<? super T> a,final Predicate<? super T> b,final Predicate<? super T>... c){
	return new Predicate<T>() {

		@Override
		public boolean test(T t) {
			if(a.test(t))
				return true;
			else if(b.test(t))
				return true;
			else for(Predicate<? super T> p:c)
				if(p.test(t))
					return true;
			return false;
		}

	};
}

@SafeVarargs
public static <T> Predicate<T> and(final Predicate<? super T> a,final Predicate<? super T> b,final Predicate<? super T>... c){
	return new Predicate<T>() {

		@Override
		public boolean test(T t) {
			if(!a.test(t))
				return false;
			else if(!b.test(t))
				return false;
			else for(Predicate<? super T> p:c)
				if(!p.test(t))
					return false;
			return true;
		}

	};
}


public static <T> Predicate<T> and(final Predicate<? super T> a,final Predicate<? super T> b){
	return new Predicate<T>() {

	@Override
	public boolean test(T t) {
		// TODO Auto-generated method stub
		return a.test(t)&&b.test(t);
	}

	};
}

public static <T> Predicate<T> not(final Predicate<? super T> a){
	return new Predicate<T>() {

		@Override
		public boolean test(T t) {
			// TODO Auto-generated method stub
			return !a.test(t);
		}

	};
}


}
