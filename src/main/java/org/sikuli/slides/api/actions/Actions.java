package org.sikuli.slides.api.actions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class Actions {
	public static void print(Action action){
		printHelper(System.out, action, 0);
	}
	
	private static void printHelper(PrintStream out, Action action, int level){
		for (int i = 0; i < level; ++i ){
			out.print("   ");
		}
		out.println(action);
		if (action instanceof RobotAction){
			CompoundAction defaultAction = (CompoundAction) action;
			for (Action child : defaultAction.getChildren()){				
				printHelper(out, child, level + 1);				
			}
		}
	}
	
	private static List<Action> collectAllActions(Action action){
		List<Action> actions = Lists.newLinkedList();
		actions.add(action);
		
		if (action instanceof CompoundAction){
			CompoundAction defaultAction = (CompoundAction) action;
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
		
		
		public boolean exist(){
			List<Action> all = all();
			return !all.isEmpty();
		}
		
		public ActionSelector isInstanceOf(final Class classz){
			ps.add(new Predicate<Action>(){
				@Override
				public boolean apply(Action action) {
					return classz.isInstance(action);					
				}				
			});
			return this;			
		}
		
		public ActionSelector isLeaf(){
			ps.add(new Predicate<Action>(){
				@Override
				public boolean apply(Action action) {
					if (action instanceof CompoundAction){
						return ((CompoundAction) action).getChildren().size() == 0;	
					}
					return false;
				}				
			});
			return this;			
		}
	}

	public static String toPrettyString(Action action) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bo);
		printHelper(out, action, 0);
		return bo.toString();
	}
}
