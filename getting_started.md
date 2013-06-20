---
layout: default
title: Getting Started
---

## Installation

1. Make sure Java is installed on your computer.
2. Download the latest jar from the [Downloads](/downloads.html) page.

## Hello World

Below is our Hello World example on Google Presentation. This example has two slides. When executed, a robot will
1. Open the browser and go to the United Nations' website [http://www.un.org/en](http://www.un.org/en).
2. Find and click the logo of the United Nations.

<iframe src="https://docs.google.com/presentation/d/1w48gExh5oLIT0J8xYXR1RxpqTrZTXJC8OR4UXxShTQ8/embed?start=false&loop=false&delayms=3000" frameborder="0" width="400" height="300" allowfullscreen="true" mozallowfullscreen="true" webkitallowfullscreen="true">
</iframe>

There are two methods to execute these slides on your own computer.

**Method 1:** Download and Execute

1. Download the slides from this link: [helloworld.pptx](https://docs.google.com/feeds/download/presentations/Export?id=1w48gExh5oLIT0J8xYXR1RxpqTrZTXJC8OR4UXxShTQ8&&exportFormat=pptx)

2. Execute the slides

{% highlight bash %}
$ java -jar sikuli-slides-1.3.0.jar helloworld.pptx 
{% endhighlight %}


**Method 2:** Execute from an URL

{% highlight bash %}
$ java -jar sikuli-slides-1.3.0.jar https://docs.google.com/presentation/d/1w48gExh5oLIT0J8xYXR1RxpqTrZTXJC8OR4UXxShTQ8/edit?usp=sharing
{% endhighlight %}

## Your Own Slides


You can create your own slides using offline using [Microsoft Powerpoint](#powerpoint) or online using [Google Presentation](#gdrive). Althought you can create slides from scratch, it would be easier to use the helloworld example as a template and make your own modifications.

<a id="powerpoint">
</a>

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

5. Execute the slides using the pptx file.

{% highlight bash %}
$ java -jar sikuli-slides-1.3.0.jar helloworld.pptx 
{% endhighlight %}

<a id="gdrive">
</a>

### Google Presentation 

1. Open the helloworld example in the Google Presentation editor. Use the following link: [https://docs.google.com/presentation/d/1w48gExh5oLIT0J8xYXR1RxpqTrZTXJC8OR4UXxShTQ8/edit?usp=sharing](https://docs.google.com/presentation/d/1w48gExh5oLIT0J8xYXR1RxpqTrZTXJC8OR4UXxShTQ8/edit?usp=sharing).	
2. Make a copy so you can edit.
3. On Slide 1, change the URL to your the URL of your own website.

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

5. Change  the Sharing setting to "Anyone who has the link can view" and locate the "Link to Share" URL. In this example, the URL is [https://docs.google.com/presentation/d/1bODdu4SOD49Z_i9Sq7qJpwmxbnnMx6ULvgNc3wQGYfw/edit?usp=sharing](https://docs.google.com/presentation/d/1bODdu4SOD49Z_i9Sq7qJpwmxbnnMx6ULvgNc3wQGYfw/edit?usp=sharing).


   <img src="/img/gdrive_link_to_share.png" class="img-polaroid" />


6. Execute the slides using the URL.

{% highlight bash %}
$ java -jar sikuli-slides-1.3.0.jar https://docs.google.com/presentation/d/1bODdu4SOD49Z_i9Sq7qJpwmxbnnMx6ULvgNc3wQGYfw/edit?usp=sharing
{% endhighlight %}


