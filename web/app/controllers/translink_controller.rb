class TranslinkController < ActionController::Base
  

  def travel

  	# required
  	from 					= params[:from]
  	to 						= params[:to]
  	at 						= params[:at]
  	
  	# not required
  	timeMode 				= params[:timeMode].present? 			? params[:timeMode] 				: 0
  	vehicleTypes 			= params[:vehicleTypes].present? 			? params[:vehicleTypes] 			: 0
  	walkSpeed 				= params[:walkSpeed].present? 				? params[:walkSpeed] 				: 0
  	maximumWalkingDistanceM = params[:maximumWalkingDistanceM].present? 	? params[:maximumWalkingDistanceM] 	: 1000
  	serviceTypes 			= params[:serviceTypes].present? 			? params[:serviceTypes] 			: 1
  	fareTypes 				= params[:fareTypes].present? 				? params[:fareTypes] 				: 1

  	search_url = "https://opia.api.translink.com.au/v2/travel/rest/plan/#{from}/#{to}?timeMode=#{timeMode}&at=#{at}&vehicleTypes=#{vehicleTypes}&walkSpeed=#{walkSpeed}&maximumWalkingDistanceM=#{maximumWalkingDistanceM}&serviceTypes=#{serviceTypes}&fareTypes=#{fareTypes}"

	c = Curl::Easy.new(search_url) do |curl| 
	  curl.headers["user-agent"] = "brisaccess"
	  curl.headers["content-type"] = "application/json"
	  curl.headers["accept"] = "application/json"
	  curl.verbose = true
	end
	c.http_auth_types = :basic
	c.username = 'christie.ethan'
	c.password = '/6Y)=anqE2_x'
	c.ssl_verify_peer = false
	c.perform

	render :json => c.body_str

  end

end