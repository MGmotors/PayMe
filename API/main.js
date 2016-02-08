window.addEventListener("load", onLoad);

var jsonNormal = null;
var outElemem = null;

function onLoad()
{
    //get the Json
    var req = new XMLHttpRequest();
    req.open("GET", "codes.json", false);
    req.send();

    jsonNormal = JSON.parse(req.responseText);

    var elem = document.getElementById("btnAndroid");
    elem.addEventListener("click", printAndroid.bind(null, jsonNormal, "", true));
    elem = document.getElementById("btnPhp");
    elem.addEventListener("click", printPHP.bind(null, jsonNormal, "", true));


    outElemem = document.getElementById("output");

}

function printAndroid(json, spaces, clear)
{
    if (clear)
    {
        outElemem.innerHTML = "";
    }
    for (var k in json)
    {
        if (isObject(json[k]))
        {
            lineOut(spaces + "public static class " + className(k) + "{");
            printAndroid(json[k], spaces + "    ");
            lineOut(spaces + "}");
        }
        else
        {
            lineOut(spaces + "public final static String " + k.toUpperCase() + ' = "' + json[k] + '";');
        }
    }

}

function className(s)
{
    return s[0].toUpperCase() + s.slice(1);
}

function isObject(obj)
{
    return obj === Object(obj);
}

function printPHP(json, spaces, clear)
{
    if (clear)
    {
        outElemem.innerHTML = "";
    }
    for (var k in json)
    {
        if (isObject(json[k]))
        {
            lineOut(spaces + "static class " + className(k) + "{");
            printPHP(json[k], spaces + "    ");
            lineOut(spaces + "}");
        }
        else
        {
            lineOut(spaces + "const " + k.toUpperCase() + ' = "' + json[k] + '";');
        }
    }
}

function lineOut(str)
{
    outElemem.innerHTML = outElemem.innerHTML + str + "\n";
}