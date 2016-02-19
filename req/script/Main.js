window.addEventListener("load",main);

function main(){
    var p = new reqMaker.Presenter();
    var v = new reqMaker.View();
    var m = new reqMaker.Model();
    
    p.setView(v);
    p.setModel(m);
    v.setPresenter(p);
    
    p.present();
}