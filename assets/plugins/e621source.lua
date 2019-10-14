function plugin(message, user) 
	if(string.find(message, "https://static1.e621.net/data") or string.find(message, "https://static1.e926.net/data"))then
		if(string.find(message, " "))then
			message = string.sub(message, 0, string.find(message, " "));
		end
		return "!e621 md5:" .. string.sub(message, string.find(message, "/[^/]*$") + 1 ,string.find(message, ".[^.]*$") - 1);
	end
end