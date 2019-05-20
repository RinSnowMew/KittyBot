function plugin(message, user) 
	if(string.find(message, "https://static1.e621.net/data"))then
		return "e621 md5:" .. string.sub(message, 37,68);
	end
end