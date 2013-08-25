Sikuli Slides Ruby
==================

[Sikuli Slides] (http://slides.sikuli.org/) allows you to interact with your application's user interface using image based search to automate user actions.

Requirements
------------

* [JRuby](http://jruby.org/download) or `rvm install jruby`


Compatibility
-------------

Run JRuby in 1.9 mode. 

    export JRUBY_OPTS=--1.9

Installation
------------

    jruby -S gem install sikuli-slides

Usage
-----

    require 'sikuli-slides'	
	Sikuli::Slides.execute "helloworld.pptx"

