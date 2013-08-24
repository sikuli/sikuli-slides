Sikuli Slides Ruby
==================

[Sikuli Slides] (http://slides.sikuli.org/) allows you to interact with your application's user interface using image based search to automate user actions.

Requirements
------------

* [JRuby](http://jruby.org/download) or `rvm install jruby`

Compatibility
-------------
It's recommended to use JRuby ~> 1.6.0 and run it in 1.9 mode to get unicode characters working as expected.

Installation
------------

    jruby -S gem install sikuli-slides

Usage
-----

    require 'sikuli-slides'	
	Sikuli::Slides.execute "helloworld.pptx"

