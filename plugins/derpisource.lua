function plugin(message, user) 
	local index;
	if(string.find(message, "https://derpicdn.net/img"))then
		if(string.find(message, "view/"))then
			index = string.find(message, "/[^/]*$");
			return "derp ID:" .. string.sub(message, index + 1, string.len(message)-5);
		else
			index = string.find(message, "/[^/]*$");
			local concat = string.sub(message, 0, index - 1);
			index = string.find(concat, "/[^/]*$");

			return "derp ID:" .. string.sub(concat, index + 1);
		end
	end
end