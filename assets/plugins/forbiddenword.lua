local time = os.time();

function plugin(message, user) 
	if(string.match(string.lower(message), "purple"))then
		local timedif = os.time() - time;
		time = os.time();
		days = (timedif / (60*60*24));
		hours = ((timedif / (60*60)) % 24); 
		mins = ((timedif / (60)) % 60); 
		secs = timedif % 60;
		
		return "*GASP!!!* \n" .. user.name .. " said **THE** word! It's been " .. round(days) .. " days, " .. round(hours) .. " hours, " .. round(mins) .. " minutes, and " .. round(secs) .. " seconds since the last time!"; 
	end
	
	return nil;
end

function round(num) 
	return math.floor(num + 0.5)
end