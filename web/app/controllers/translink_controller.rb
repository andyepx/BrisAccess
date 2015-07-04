class TranslinkController < ActionController::Base
  

  def travel

  	# required
  	from 					= params[:from]
  	to 						= params[:to]
  	at 						= params[:at]
  	
  	# not required
  	timeMode 				= params[:timeMode].present? 			? params[:timeMode] 				: "0"
  	vehicleTypes 			= params[:vehicleTypes].present? 			? params[:vehicleTypes] 			: "0"
  	walkSpeed 				= params[:walkSpeed].present? 				? params[:walkSpeed] 				: "0"
  	maximumWalkingDistanceM = params[:maximumWalkingDistanceM].present? 	? params[:maximumWalkingDistanceM] 	: "1000"
  	serviceTypes 			= params[:serviceTypes].present? 			? params[:serviceTypes] 			: "1"
  	fareTypes 				= params[:fareTypes].present? 				? params[:fareTypes] 				: "1"

  	#  from = URI::escape(from)
  	from = URI.escape(from, Regexp.new("[^#{URI::PATTERN::UNRESERVED}]"))
  	to = URI.escape(to, Regexp.new("[^#{URI::PATTERN::UNRESERVED}]"))
  	at = URI.escape(at, Regexp.new("[^#{URI::PATTERN::UNRESERVED}]"))
  	
  	search_url = "https://opia.api.translink.com.au/v2/travel/rest/plan/"+from+"/"+to+"?timeMode=0&at="+at+"&walkSpeed=0&maximumWalkingDistanceM=2000"

	c = Curl::Easy.new(search_url) do |curl| 
	  	curl.headers["Content-Type"] = "application/json"
	  	curl.headers["Accept"] = "application/json"
	  	curl.headers["X-HTTP-Method-Override"] = "GET"
	  	curl.verbose = true
	end
	c.http_auth_types = :basic
	c.username = 'christie.ethan'
	c.password = '/6Y)=anqE2_x'
	c.ssl_verify_peer = false
	c.perform

	puts c

	render :json => c.body_str

  end

  def suggest

  	# required
  	input = params[:input]
  	input = URI.escape(input, Regexp.new("[^#{URI::PATTERN::UNRESERVED}]"))
  	
  	search_url = "https://opia.api.translink.com.au/v2/location/rest/suggest?input=#{input}&filter=0&maxResults=5"

	c = Curl::Easy.new(search_url) do |curl| 
	  	curl.headers["Content-Type"] = "application/json"
	  	curl.headers["Accept"] = "application/json"
	  	curl.headers["X-HTTP-Method-Override"] = "GET"
	  	curl.verbose = true
	end
	c.http_auth_types = :basic
	c.username = 'christie.ethan'
	c.password = '/6Y)=anqE2_x'
	c.ssl_verify_peer = false
	c.perform

	puts c

	render :json => c.body_str

  end

end