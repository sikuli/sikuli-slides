package org.sikuli.slides.api;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.Actions;
import org.sikuli.slides.api.actions.BookmarkAction;
import org.sikuli.slides.api.models.Slide;


public interface ExecutionFilter {
	public boolean accept(ExecutionEvent slide);
	
	
	// Factory class for easy creation of commonly used filters
	static public class Factory {
		static public ExecutionFilter createSingleSlideFilter(final int number){
			return new ExecutionFilter(){
				@Override
				public boolean accept(ExecutionEvent event) {
					return event.getSlide().getNumber() == number;						
				}
			};
		}
		
		static public ExecutionFilter createStartFromSlideFilter(final int number){
			return new ExecutionFilter(){
				@Override
				public boolean accept(ExecutionEvent event) {
					return event.getSlide().getNumber() >= number;						
				}
			};
		}
		
		static public ExecutionFilter createRangeFilter(final int start, final int end){
			return new ExecutionFilter(){
				@Override
				public boolean accept(ExecutionEvent event) {
					Slide slide = event.getSlide();
					return slide.getNumber()  >= start && slide.getNumber()  <= end;						
				}
			};
		}
		
		static public ExecutionFilter createAllFilter(){
			return new ExecutionFilter(){
				@Override
				public boolean accept(ExecutionEvent event) {
					return true;						
				}
			};
		}
		
		static public ExecutionFilter createStartFromBookmarkFilter(String bookmarkName){
			return new StartFromBookmarkFilter(bookmarkName);
		}
		
		static class StartFromBookmarkFilter implements ExecutionFilter{
			public StartFromBookmarkFilter(String bookmarkName) {
				this.bookmarkName = bookmarkName;
			}

			boolean isBookmarkFound = false;
			final String bookmarkName;

			@Override
			public boolean accept(ExecutionEvent slide) {
				// if a bookmark has already been found before
				if (isBookmarkFound)
					// this slide must be after the bookmark
					return true;				
				
				// see if the current action is a matching bookamark
				Action action = slide.getAction();
				BookmarkAction bookmarkAction = (BookmarkAction) Actions.select(action).isInstanceOf(BookmarkAction.class).first();			
				if (bookmarkAction != null && bookmarkAction.getName().compareToIgnoreCase(bookmarkName)==0){
					isBookmarkFound = true;
					return true;
				}								
				return false;
			}
		
		}

	}
}