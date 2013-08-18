package org.sikuli.slides.api;

import static com.google.common.base.Preconditions.checkNotNull;

import org.sikuli.slides.api.actions.Action;
import org.sikuli.slides.api.actions.Actions;
import org.sikuli.slides.api.actions.BookmarkAction;
import org.sikuli.slides.api.models.Slide;


/**
 * Interface to filter a set of execution events.
 * 
 * @author Sikuli Lab
 */
public interface ExecutionFilter {
	/** Whether or not to accept this event
	 * @param event	the event to decide
	 * @return	true to accept, false to reject
	 */
	public boolean accept(ExecutionEvent event);

	/**
	 * Creates commonly used filters
	 * 
	 * @author Sikuli Lab
	 */
	static public class Factory {
		
		private Factory(){}
		
		/**
		 * Get an instance of the filter that will pick only the slide associated with the given number 
		 * @param number	the number
		 * @return	the filter
		 */
		static public ExecutionFilter createSingleSlideFilter(final int number){
			return new ExecutionFilter(){
				@Override
				public boolean accept(ExecutionEvent event) {
					return event.getSlide().getNumber() == number;						
				}
			};
		}
		
		/**
		 * Get an instance of the filter that will pick slides starting from the given number
		 * @param number	the number from which to pick
		 * @return	the filter
		 */
		static public ExecutionFilter createStartFromSlideFilter(final int number){
			return new ExecutionFilter(){
				@Override
				public boolean accept(ExecutionEvent event) {
					return event.getSlide().getNumber() >= number;						
				}
			};
		}
		
		/**
		 * Get an instance of the filter that will pick a range of slides from i to the j
		 * @param i	the number of the first slide with the range
		 * @param j	the number of the last slide within the range
		 * @return	the filter
		 */
		static public ExecutionFilter createRangeFilter(final int i, final int j){
			return new ExecutionFilter(){
				@Override
				public boolean accept(ExecutionEvent event) {
					Slide slide = event.getSlide();
					return slide.getNumber()  >= i && slide.getNumber()  <= j;						
				}
			};
		}
		
		/**
		 * Get an instance of the filter that will pick ALL the slides
		 * @return	the filter
		 */
		static public ExecutionFilter createAllFilter(){
			return new ExecutionFilter(){
				@Override
				public boolean accept(ExecutionEvent event) {
					return true;						
				}
			};
		}
		
		/**
		 * Get an instance of the filter that will pick all slides starting from a given bookmark.
		 * If the bookmark does not exist, it picks nothing. 
		 * @param bookmarkName	the name of the bookmark
		 * @return	the filter
		 */
		static public ExecutionFilter createStartFromBookmarkFilter(String bookmarkName){
			checkNotNull(bookmarkName);
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