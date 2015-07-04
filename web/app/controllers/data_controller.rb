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

  end

  def qr

  	station = params[:station]
  	stationId = params[:sid]

  	file = File.read(Rails.root.join('public', 'qr_accessibility_2.json'))
  	data = JSON.parse(file)

  	return_data = []

  	if station or stationId
	  	data.each do |child|
	  		if station and ( (child['Station'].downcase.include? station.downcase) or (station.downcase.include? child['Station'].downcase) )
	    		return_data.push(child)
	    	elsif stationId and child['stopIds'].include? stationId
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

  def ferries

  	stop = params[:stop]

  	file = File.read(Rails.root.join('public', 'ferries_accessibility.json'))
  	data = JSON.parse(file)

  	return_data = []

  	if stop
	  	data.each do |child|
	  		if child['Terminal Name'].downcase.eql? stop.downcase
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

end
