require 'sikuli-slides'
#Sikuli::Slides.execute "helloworld.pptx", :logLevel => "INFO", :screen => 1
["Adam","Ben","Cindy"].each do |name|
  Sikuli::Slides.execute "hellouser.pptx", :params => {:user => name}
end



success = false
dicitionary.each do |word|
  begin
    Sikuli::Slides.execute "login.pptx", :params => {:user => "admin", :password => word}
    puts "Dictionary attack is successful!"
    return
  rescue TargetNotFound => e    
  end
end