require 'sikuli-slides'
#Sikuli::Slides.execute "helloworld.pptx", :logLevel => "INFO", :screen => 1
#["Adam","Ben","Cindy"].each do |name|
##  Sikuli::Slides.execute "../hellouser.pptx", :params => {:user => name}
#  
#end

# Sikuli::Slides.execute "http://localhost:4000/examples/gui/helloun/helloworld.pptx"
#  Sikuli::Slides.execute "http://localhost:4000/examples/player/kingdomrush/kingdomrush.pptx"


Sikuli::API.browse "http://www.xe.com/currencyconverter/#converter"

slidesUrl = "http://localhost:4000/examples/player/xe/convert.pptx"

10.times do 
  x = (1..1000).to_a.sample

  Sikuli::Slides.execute slidesUrl, :params => {:amount => x}
end
  
#success = false
#dicitionary.each do |word|
#  begin
#    Sikuli::Slides.execute "login.pptx", :params => {:user => "admin", :password => word}
#    puts "Dictionary attack is successful!"
#    return
#  rescue TargetNotFound => e    
#  end
#end