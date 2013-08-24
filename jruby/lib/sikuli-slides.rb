require 'sikuli-slides/slides'
require 'sikuli-slides/exception'
Sikuli::Slides.load_jars!
level = Java::Ch.qos.logback.classic.Level.toLevel("INFO")
#puts level
#java_import 'ch/qos/logback/classic/Logger'
org.slf4j.LoggerFactory.getLogger("org.sikuli.slides").setLevel level