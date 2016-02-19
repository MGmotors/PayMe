window.reqMaker = window.reqMaker ||
{};

window.reqMaker.Model = function()
{
    var json;

    this.init = function()
    {
        var req = new XMLHttpRequest();
        req.open("GET", "/API/codes.json", false);
        req.send();
        json = JSON.parse(req.responseText);
    };

    this.getHeaderFields = function()
    {
        return Object.keys(json.HeaderFields);
    };
    this.getActionNames = function()
    {
        return Object.keys(json.ActionCodes);
    };
    this.getActionCodeByName = function(name)
    {
        return json.ActionCodes[name];
    };

    this.getStatusNameByCode = function(code)
    {
        for (var name in json.ErrorCodes)
        {
            if (json.ErrorCodes[name] == code)
            {
                return name;
            }
        }
        return "unknown";
    };

    this.getHeaderFieldFromName = function(name)
    {
        return json.HeaderFields[name];
    };

    this.getURLs = function()
    {
        var a = [];
        for (var s in json.URLs)
        {
            a.push(json.URLs[s]);
        }
        return a;
    };

};
