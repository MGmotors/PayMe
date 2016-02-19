window.reqMaker = window.reqMaker||{};

window.reqMaker.Presenter = function(){
    var m;
    var v;
    
    this.present = function(){
        m.init();
        v.init();
    };
    
    this.setModel = function(model){
        m = model;
    };
    
    this.setView = function(view){
        v = view;
    };
    
    this.getHeaderFields = function(){
        return m.getHeaderFields();
    };
    
    this.getSites = function(){
        return m.getURLs();  
    };
    
    this.getActions = function(){
        return m.getActionNames();
    };
    
    this.launchRequest = function(headerNames,headerValues, action,url){
        var str1;
        var ret;
        ret = "";
        str1 = "";
        str1 += m.getHeaderFieldFromName("ACTION") + "=" + m.getActionCodeByName(action)+"&";
        for(var i in headerNames){
            str1 += m.getHeaderFieldFromName(headerNames[i]) + "=" + headerValues[i] + "&"; 
        }
        str1 = str1.slice(0, -1);
       
        var req = new XMLHttpRequest();
        req.withCredentials = true;
        req.open("POST", url, false);
        req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        req.send(str1);
        v.wasError(req.getResponseHeader("payme-error"));
        ret += "### Request Data ### \n";
        ret += str1 + "\n\n";
        ret += "### Response header ### \n";
        ret += req.getAllResponseHeaders() + "\n";
        ret += "### Response text ### \n";
        ret += req.responseText;
        v.print(ret);
    };
    
      
};