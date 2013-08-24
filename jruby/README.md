Sikuli Slides Ruby
==================

[Sikuli Slides] (http://slides.sikuli.org/) allows you to interact with your application's user interface using image based search to automate user actions.

Requirements
------------

* [JRuby](http://jruby.org/download) or `rvm install jruby`
* [Sikuli Slides 1.4.0](http://slides.sikuli.org/)

Compatibility
-------------
It's recommended to use JRuby ~> 1.6.0 and run it in 1.9 mode to get unicode characters working as expected.

```
export JRUBY_OPTS=--1.9
```

On Windows or Linux make sure to set SIKULI_SLIDES_HOME to the Sikuli Slides' installation directory and to add the Sikuli installation directory and Sikuli libs directory to the include path.

```
export SIKULI_SLIDES_HOME="~/bin/Sikuli-X-1.0rc3/Sikuli-IDE/"
PATH="${PATH}:~/bin/Sikuli-X-1.0rc3/Sikuli-IDE/:~/bin/Sikuli-X-1.0rc3/Sikuli-IDE/libs/"
```

Installation
------------

    gem install sikuli-slides

Usage
-----

    require 'java'
    require 'sikuli-slides'
	
	Sikuli::Slides.execute "helloworld.pptx"

