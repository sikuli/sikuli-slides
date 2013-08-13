package org.sikuli.slides.api.actions;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;


public class Actions {
	public static void print(Action action){
		printHelper(action, 0);
	}
	
	private static void printHelper(Action action, int level){
		for (int i = 0; i < level; ++i ){
			System.out.print("   ");
		}
		System.out.println(action);
		if (action instanceof DefaultAction){
			DefaultAction defaultAction = (DefaultAction) action;
			for (Action child : defaultAction.getChildren()){				
				printHelper(child, level + 1);				
			}
		}
	}
	
	private static List<Action> collectAllActions(Action action){
		List<Action> actions = Lists.newLinkedList();
		actions.add(action);
		
		if (action instanceof DefaultAction){
			DefaultAction defaultAction = (DefaultAction) action;
			for (Action child : defaultAction.getChildren()){				
				actions.addAll(collectAllActions(child));		
			}
		}
		return actions;
	}
	
	public static ActionSelector select(Action action){
		return new ActionSelector(collectAllActions(action));
	}
	
	static public class ActionSelector {
		Collection<Action> list;
		List<Predicate<Action>> ps = Lists.newArrayList();
		public ActionSelector(Collection<Action> list) {
			this.list = list;
		}		
		
		public List<Action> all(){
			return Lists.newArrayList(
					Collections2.filter(list, new Predicate<Action>(){
				@Override
				public boolean apply(Action a) {
					for (Predicate<Action> p : ps){
						if (!p.apply(a))
							return false;
					}					
					return true;
				}				
			}));
		}
		
		public Action first(){
			List<Action> all = all();
			if (all.isEmpty()){
				return null;
			}else{
				return all.get(0);
			}
		}
		
		public ActionSelector isInstaceOf(final Class classz){
			ps.add(new Predicate<Action>(){
				@Override
				public boolean apply(Action action) {
					return action.getClass() == classz;					
				}				
			});
			return this;			
		}
	}
}
