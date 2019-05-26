function plugin(message, user) 
	if(string.find(message, "https://static1.e621.net/data") or string.find(message, "https://static1.e926.net/data"))then
		return "!e621 md5:" .. string.sub(message, string.find(message, "/[^/]*$") + 1 ,string.find(message, ".[^.]*$") - 1);
	end
end