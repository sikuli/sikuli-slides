package org.sikuli.slides.api;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.Actions;
import org.sikuli.slides.api.actions.BookmarkAction;
import org.sikuli.slides.api.models.Slide;


public interface SlideExecutionEventFilter {
	public boolean accept(SlideExecutionEvent slide);
	
	
	// Factory class for easy creation of commonly used filters
	static public class Factory {
		static public SlideExecutionEventFilter createSingleSlideFilter(final int number){
			return new SlideExecutionEventFilter(){
				@Override
				public boolean accept(SlideExecutionEvent event) {
					return event.getSlide().getNumber() == number;						
				}
			};
		}
		
		static public SlideExecutionEventFilter createStartFromSlideFilter(final int number){
			return new SlideExecutionEventFilter(){
				@Override
				public boolean accept(SlideExecutionEvent event) {
					return event.getSlide().getNumber() >= number;						
				}
			};
		}
		
		static public SlideExecutionEventFilter createRangeFilter(final int start, final int end){
			return new SlideExecutionEventFilter(){
				@Override
				public boolean accept(SlideExecutionEvent event) {
					Slide slide = event.getSlide();
					return slide.getNumber()  >= start && slide.getNumber()  <= end;						
				}
			};
		}
		
		static public SlideExecutionEventFilter createAllFilter(){
			return new SlideExecutionEventFilter(){
				@Override
				public boolean accept(SlideExecutionEvent event) {
					return true;						
				}
			};
		}
		
		static public SlideExecutionEventFilter createStartFromBookmarkFilter(String bookmarkName){
			return new StartFromBookmarkFilter(bookmarkName);
		}
		
		static class StartFromBookmarkFilter implements SlideExecutionEventFilter{
			public StartFromBookmarkFilter(String bookmarkName) {
				this.bookmarkName = bookmarkName;
			}

			boolean isBookmarkFound = false;
			final String bookmarkName;

			@Override
			public boolean accept(SlideExecutionEvent slide) {
				// if a bookmark has already been found before
				if (isBookmarkFound)
					// this slide must be after the bookmark
					return true;				
				
				// see if the current action is a matching bookamark
				Action action = slide.getAction();
				BookmarkAction bookmarkAction = (BookmarkAction) Actions.select(action).isInstaceOf(BookmarkAction.class).first();			
				if (bookmarkAction != null && bookmarkAction.getName().compareToIgnoreCase(bookmarkName)==0){
					isBookmarkFound = true;
					return true;
				}								
				return false;
			}
		
		}

	}
}