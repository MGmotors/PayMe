window.reqMaker = window.reqMaker||{};
window.reqMaker.View = function(){
    var p;
    var chbLink;
    var chbAction;
    var headerCon;
    //var myData = {};
    var headerCHBs = [];
    var headerTBs = [];
    var output;
    var nextFreeName = 0;
    var indicator;
    
    this.init = function(){
        var body = document.getElementsByTagName("body")[0];
        var ueberschrift = document.createElement("h1");
        ueberschrift.innerHTML = "Click your Request";
        body.appendChild(ueberschrift);
        
        var linkCon = document.createElement("div");
        linkCon.appendChild(document.createTextNode("Link zur Seite: "));
        chbLink = createDropDown(p.getSites());
        //chbLink = document.createElement("input");
        //chbLink.setAttribute("type", "text");
        //chbLink.style.width="400px";
        linkCon.appendChild(chbLink);
        body.appendChild(linkCon);
        
        var actionCon = document.createElement("div");
        actionCon.appendChild(document.createTextNode("Action : "));
        chbAction = createDropDown(p.getActions());
        chbAction.style["margin-left"]="43px";
        actionCon.style["margin-top"] = "5px";
        actionCon.appendChild(chbAction);
        body.appendChild(actionCon);
        
        headerCon = document.createElement("div");
        headerCon.style["margin-top"]="20px";
        var btn1 = document.createElement("button");
        btn1.addEventListener("click",this.addRow.bind(this));
        btn1.innerHTML = "add Post Data";
        headerCon.appendChild(btn1);
        body.appendChild(headerCon);
        
        var fireCon = document.createElement("div");
        var btnFire = document.createElement("button");
        btnFire.innerHTML = "launch request";
        btnFire.addEventListener("click", launch.bind(this));
        fireCon.style["margin-top"] = "15px";
        fireCon.appendChild(btnFire);
        body.appendChild(fireCon);
        
        indicator = document.createElement("div");
        indicator.style["margin-left"] = "0px";
        indicator.style.width = "600px";
        indicator.style.height = "20px";
        indicator.style.textAlign = "center";
        indicator.style["margin-top"] = "15px";
        body.appendChild(indicator);
        
        output =  document.createElement("pre");
        output.style.width="600px";
        output.style.minHeight="600px";
        output.style.border="solid 1px black";
        body.appendChild(output);
    };
    
    this.setPresenter = function(presenter){
        p = presenter;
    };
    
    this.addRow = function(){
        var id = nextFreeName;
        var div = document.createElement("div");
        div.style["margin-top"] = "10px";
        div.setAttribute("name", id);
        var values = p.getHeaderFields();
        var select1 = createDropDown(values);
        var txt1 = document.createElement("input");
        txt1.setAttribute("type","text");
        txt1.style.width="250px";
        txt1.style["margin-left"] = "10px";
        headerCHBs["'" + id + "'"] = select1;
        headerTBs["'" + id + "'"] = txt1;
        var btn = document.createElement("button");
        btn.addEventListener("click", this.deleteRow.bind(this,div));
        btn.innerHTML = "delete";
        btn.style["margin-left"] = "10px";
        
        div.appendChild(select1);
        div.appendChild(txt1);
        div.appendChild(btn);
        headerCon.appendChild(div);
        nextFreeName ++;
    };
    
    var createDropDown = function(names){
        var select = document.createElement("select");
        for (var n in names){
            var op = document.createElement("option");
            op.setAttribute("value",names[n]);
            op.innerHTML= names[n];
            select.appendChild(op);
        }
        return select;
    };
    
    var launch = function(){
        var values = [];
        var names = [];
        for(var i in headerCHBs){
           names.push(headerCHBs[i].value);
           values.push(headerTBs[i].value);
        }
        p.launchRequest(names,values,chbAction.value,chbLink.value);
    };
    
    this.deleteRow = function(rowCon){
        var id = rowCon.getAttribute("name");
        delete headerCHBs[id];
        delete headerTBs[id];
        headerCon.removeChild(rowCon);
    };
    
    this.wasError = function(code){
        indicator.innerHTML = code;
        if(code != "0"){
            indicator.style.backgroundColor = "#FFE6E6";
        }else{
           indicator.style.backgroundColor = "#DDF9DD";
        }
        
    };
    
    this.print = function(text){
        output.innerHTML = text;
    };
    
    
};