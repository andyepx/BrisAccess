class DataController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception

  def locations

  	suburb = params[:suburb]
  	location = params[:status]
  	category = params[:category]
  	rating = params[:rating]

  	file = File.read(Rails.root.join('public', 'locations_accessibility.json'))
  	data = JSON.parse(file)

  	return_data = []

  	if suburb and !category
	  	data.each do |child|
	  		if child['suburb'].downcase.include? suburb.downcase
	    		return_data.push(child)
	    	end
		end
	end

	if category and !suburb
	  	data.each do |child|
	  		if child['category'].downcase.include? category.downcase
	    		return_data.push(child)
	    	end
		end
	end

  	if return_data.length > 0
	  	render :json => return_data
	else
		render :json => data

  end

  def qr

  	station = params[:station]

  	file = File.read(Rails.root.join('public', 'qr_accessibility.json'))
  	data = JSON.parse(file)

  	return_data = []

  	if station
	  	data.each do |child|
	  		if child['Station'].downcase.eql? station.downcase
	    		return_data.push(child)
	    	end
		end
	end

	if return_data.length > 0
	  	render :json => return_data
	else
		render :json => data
		
  end

  def areas

  	suburb = params[:suburb]

  	file = File.read(Rails.root.join('public', 'suburbs_accessibility.json'))
  	data = JSON.parse(file)

  	return_data = []

  	if suburb
	  	data.each do |child|
	  		if child['Suburb_Name'].downcase.eql? suburb.downcase
	    		return_data.push(child)
	    	end
		end
	end

  	if return_data.length > 0
	  	render :json => return_data
	else
		render :json => data

  end

end
