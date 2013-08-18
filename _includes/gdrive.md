
### Google Presentation 

1. Open the helloworld example in the Google Presentation editor. Use the following link: [https://docs.google.com/presentation/d/1w48gExh5oLIT0J8xYXR1RxpqTrZTXJC8OR4UXxShTQ8/edit?usp=sharing](https://docs.google.com/presentation/d/1w48gExh5oLIT0J8xYXR1RxpqTrZTXJC8OR4UXxShTQ8/edit?usp=sharing).	
2. Make a copy so you can edit.
3. On Slide 1, change the URL to the URL of your own website.

   <table>
	<tr>
		<td style="padding:10px" width="30%"><img src="/img/gdrive_slide1.png" class="img-polaroid"/></td>
		<td style="padding:10px" width="30%"><img src="/img/gdrive_change_url.png"  class="img-polaroid"/></td>			
		<td style="padding:10px" width="30%"></td>
	</tr>
   </table>

4. On Slide 2, change the screenshot to one of your own website. Then adjust the location and size of the red rectangle to cover the target
   to click, in this case, the "Contact" tab.


   <table>
	<tr>
		<td style="padding:10px" width="30%"><img src="/img/gdrive_slide2.png" class="img-polaroid"/></td>
		<td style="padding:10px" width="30%"><img src="/img/gdrive_change_screenshot.png" class="img-polaroid"/></td>			
		<td style="padding:10px" width="30%"><img src="/img/gdrive_adjust_box.png" class="img-polaroid"/></td>
	</tr>
   </table>

5. Change the Sharing setting to "Anyone who has the link can access. No sign-in required." and locate the "Link to Share" URL. In this example, the URL is [https://docs.google.com/presentation/d/1bODdu4SOD49Z_i9Sq7qJpwmxbnnMx6ULvgNc3wQGYfw/edit?usp=sharing](https://docs.google.com/presentation/d/1bODdu4SOD49Z_i9Sq7qJpwmxbnnMx6ULvgNc3wQGYfw/edit?usp=sharing).


   <img src="/img/gdrive_link_to_share.png" class="img-polaroid" />


6. Open the slides from the URL in sikuli-slides and execute them. There are two ways to run sikuli-slides: from the GUI and from the command line.

#####  A) Using the Graphical User Interface (GUI)
1. Double click on the jar file. <br/>
2. Click on open URL. <br/>
<img src="/img/sikuli-slides_openURL.png"/>
3. Copy the following link: https://docs.google.com/presentation/d/1bODdu4SOD49Z_i9Sq7qJpwmxbnnMx6ULvgNc3wQGYfw/edit?usp=sharing and paste it in the open remote presentation dialog. <br/>
<img src="/img/sikuli-slides_openURL_Dialog.png" class="img-polaroid" />	
4. Click OK and wait until the file is downloaded.<br/>
5. Click Run.<br/>
<img src="/img/sikuli-slides_runURL.png" class="img-polaroid" />
6. sikuli-slides will minimize the window, so it can see what's on the screen and execute the slides.<br/>

#####  B) Using the command line tool:
	
1. Open the terminal app or the command prompt.
2. cd to the location of sikuli-slides jar file.
3. Type the following command and hit enter.
{% highlight bash %}
$ java -jar sikuli-slides-1.3.0.jar https://docs.google.com/presentation/d/1bODdu4SOD49Z_i9Sq7qJpwmxbnnMx6ULvgNc3wQGYfw/edit?usp=sharing
{% endhighlight %}
4. Minimize the terminal/command prompt window or any other window, so sikuli-slides can see what's on the screen and execute the slides.