function plugin(message, user) 
	local index;
	local concat;
	if(string.find(message, "https://derpicdn.net/img"))then
		if(string.find(message, "view/"))then
			index = string.find(message, "/[^/]*$");
			return "!derp ID:" .. string.sub(message, index + 1, string.find(message, ".[^.]*$")-1);
		else
			if(string.find(message, "download/"))then
				index = string.find(message, "_");
				concat = string.sub(message, 0, index-1);
				return "!derp ID:" .. string.sub(concat, string.find(message, "/[^/]*$") + 1);
			else
				index = string.find(message, "/[^/]*$");
				concat = string.sub(message, 0, index - 1);
				index = string.find(concat, "/[^/]*$");

				return "!derp ID:" .. string.sub(concat, index + 1);
			end
		end
	end
end