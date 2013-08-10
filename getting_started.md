---
layout: default
title: Getting Started
---

## Installation

1. Make sure that the Java Runtime Environment (JRE) is installed on your computer.
2. Download the latest jar from the [Downloads](/downloads.html) page.


## Hello World

Below is our Hello World example on Google Presentation. This example has two slides. When executed, a robot will
1. Open the browser and go to the United Nations' website [http://www.un.org/en](http://www.un.org/en).
2. Find and click the logo of the United Nations.

<iframe src="https://docs.google.com/presentation/d/1w48gExh5oLIT0J8xYXR1RxpqTrZTXJC8OR4UXxShTQ8/embed?start=false&amp;loop=false&amp;delayms=3000" frameborder="0" width="400" height="300" allowfullscreen="true" mozallowfullscreen="true" webkitallowfullscreen="true">
</iframe>

There are two methods to open the slides in sikuli-slides and execute them on your own computer.

**Method 1:** Download, Open, and Execute

1. Download the slides from this link:
[helloworld.pptx](https://docs.google.com/feeds/download/presentations/Export?id=1w48gExh5oLIT0J8xYXR1RxpqTrZTXJC8OR4UXxShTQ8&&exportFormat=pptx)

2. Open and execute the slides

{% highlight bash %}
$ java -jar sikuli-slides-1.3.0.jar helloworld.pptx 
{% endhighlight %}


**Method 2:** Open and execute from a URL

{% highlight bash %}
$ java -jar sikuli-slides-1.3.0.jar https://docs.google.com/presentation/d/1w48gExh5oLIT0J8xYXR1RxpqTrZTXJC8OR4UXxShTQ8/edit?usp=sharing
{% endhighlight %}

## Your Own Slides


You can make your own slides locally using Microsoft Powerpoint or online using Google Presentation. The presentation file must be in .pptx format.
A slide may contain a screenshot and a set of annotations. The screenshot can be of any part of a user interface, such as a button, a group of buttons, a form, a dialog box, a window, or the entire desktop. 
The annotations are shapes such as rectangles, ovals, etc, to indicate targets and text boxes to indicate actions to be performed on the targets.
Targets refere to any User Interface component such as icons, buttons, etc. Actions refer to mouse and keyboard events such as Click, Right Click, 
Double Click, Drag and drop, Type, and other actions such as browser and wait. A slide may contain one or more targets and only one action.
Although you can make slides from scratch, it would be easier to use the helloworld example as a template and add your own contents.


<ul class="nav nav-tabs" id="myTabs">
  <li class="active"><a href="#gdrive" data-toggle="tab">Google Presentation</a></li>
  <li><a href="#powerpoint" data-toggle="tab">Microsoft Powerpoint (.pptx)</a></li>
</ul>

<div class="tab-content">
  <div class="tab-pane active" id="gdrive">


	{% capture content_gdrive %}
	    {% include gdrive.md %}
	  {% endcapture %}
	{{ content_gdrive | unindent | markdownify }}


	
  </div>
  <div class="tab-pane" id="powerpoint">

	{% capture content_powerpoint %}
	    {% include powerpoint.md %}
	  {% endcapture %}
	{{ content_powerpoint | unindent | markdownify }}


  </div>
</div>

<script>
  $(function () {
    $('#myTab a:last').tab('show');
  })
</script>


