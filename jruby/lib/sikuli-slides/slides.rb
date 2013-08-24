module Sikuli
  class Slides        
    
    def self.jars_root
          File.join("#{File.dirname(__FILE__)}", "jars")
    end

    def self.load_jars!
          require 'java'
          puts jars_root
          Dir["#{jars_root}/*.jar"].each {|jar| 
            require jar 
          }
    end
    
    def self.execute(filename)
      begin
        org.sikuli.slides.api.Slides.execute(java.io.File.new(filename))
      rescue org.sikuli.slides.api.SlideExecutionException => e
        if e.message.match "target"
          raise TargetNotFound
        end
      end
    end    
    
    def self.createContext(params = {})
      context = org.sikuli.slides.api.Context.new
      if params[:minScore]
        context.setMinScore(params[:minScore])
      end
      if params[:waitTime]
        context.setWaitTime(params[:waitTime])
      end      
      if params[:logLevel]
        level = Java::Ch.qos.logback.classic.Level.toLevel(params[:logLevel])
        if level
          org.slf4j.LoggerFactory.getLogger("org.sikuli.slides").setLevel level
        else
          level = Java::Ch.qos.logback.classic.Level.toLevel("INFO")
          org.slf4j.LoggerFactory.getLogger("org.sikuli.slides").setLevel 
        end
      end
      if params[:params]
        params[:params].each do |key, val|
          context.addParameter(key, val)
        end
      end
      if params[:start]
        if params[:start].is_a?(String)
          filter = org.sikuli.slides.api::ExecutionFilter::Factory.createStartFromBookmarkFilter params[:start]
        else
          filter = org.sikuli.slides.api::ExecutionFilter::Factory.createStartFromSlideFilter params[:start]
        end
        context.setExecutionFilter(filter)
      end       
      if params[:screen]
        context.setScreenRegion(org.sikuli.api.DesktopScreenRegion.new(params[:screen]))
      end
      context
    end
    
    def self.execute(filename, params = {} )
      begin
        context = createContext(params)
        org.sikuli.slides.api.Slides.execute(java.io.File.new(filename), context)
      rescue org.sikuli.slides.api.SlideExecutionException => e
        if e.message.match "target"
          raise TargetNotFound
        end
      end
    end    
    
  end
end