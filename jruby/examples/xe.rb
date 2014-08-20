require 'sikuli-slides'

# location to the online currency converter
Sikuli::API.browse "http://www.xe.com/currencyconverter/#converter"

# location to download slides
slidesUrl = "http://slides.sikuli.org/examples/player/xe/convert.pptx"

# execute the slides 10 times
10.times do 
  # each time, a random number between 1 and 1000 is generated
  x = (1..1000).to_a.sample

  # this number is given as a parameter to the slides
  Sikuli::Slides.execute slidesUrl, :params => {:amount => x}
end