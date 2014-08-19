# -*- encoding: utf-8 -*-
$:.push File.expand_path("../lib", __FILE__)

Gem::Specification.new do |s|
  s.name        = "sikuli-slides"
  s.version     = "1.5.0"
  s.authors     = ["Khalid Alharbi", "Tom Yeh"]
  s.email       = ["khalid.alharbi@colorado.edu", "tom.yeh@colorado.edu"]
  s.homepage    = "http://lab.sikuli.org"
  s.summary     = %q{Ruby wrapper for Sikuli Slides}
  s.description = %q{Sikuli Slides allows you to execute presentation slides to interact user interfaces.}
  s.platform      = 'java'

  s.files         = `git ls-files`.split("\n")
  s.test_files    = `git ls-files -- {test,spec,features}/*`.split("\n")
  s.executables   = `git ls-files -- bin/*`.split("\n").map{ |f| File.basename(f) }
  s.require_paths = ["lib"]
end
