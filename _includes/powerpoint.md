
### Microsoft PowerPoint (.pptx)

1. Download the helloworld example slides as a .pptx file: [helloworld.pptx](https://docs.google.com/feeds/download/presentations/Export?id=1w48gExh5oLIT0J8xYXR1RxpqTrZTXJC8OR4UXxShTQ8&&exportFormat=pptx)
2. Open the file in Microsoft PowerPoint. It must be a version that supports the pptx format.
3. On Slide 1, change the URL to your the URL of your own website.

   <table>
	<tr>
		<td style="padding:10px" width="30%"><img class="img-polaroid" src="/img/powerpoint_slide1.png"/></td>
		<td style="padding:10px" width="30%"><img class="img-polaroid" src="/img/powerpoint_change_url.png"/></td>			
		<td style="padding:10px" width="30%"></td>
	</tr>
   </table>

4. On Slide 2, change the screenshot to one of your own website. Then adjust the location and size of the red rectangle to cover the target
   to click, in this case, the "Contact" tab.

   <table>
	<tr>
		<td style="padding:10px" width="30%"><img class="img-polaroid" src="/img/powerpoint_slide2.png"/></td>
		<td style="padding:10px" width="30%"><img class="img-polaroid" src="/img/powerpoint_change_screenshot.png"/></td>			
		<td style="padding:10px" width="30%"><img class="img-polaroid" src="/img/powerpoint_adjust_box.png"/></td>
	</tr>
   </table>

5. Open the pptx file in sikuli-slides and execute the slides.

{% highlight bash %}
$ java -jar sikuli-slides-1.3.0.jar helloworld.pptx 
{% endhighlight %}



