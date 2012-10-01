package beforeclass;


public class Formula9_backtracking_search {
    // datatype definition:
    //    Formula = Var(name:String) 
    //              + Not(f:Formula)
    //              + And(left:Formula, right:Formula)
    //              + Or(left:Formula, right:Formula)
	//              + ForAll(var:String, f:Formula)
    //              + ThereExists(var:String, f:Formula)
        
    public static interface Formula {
        public interface Visitor<R> {
            public R on(Var v);
            public R on(Not n);
            public R on(And a);
            public R on(Or o);
            public R on(ForAll a);
            public R on(ThereExists e);
        }
        public <R> R accept(Visitor<R> v);        
    }
    
    public static class Var implements Formula{
        public final String name;
        public Var(String name) { this.name = name; }
        public <R> R accept(Visitor<R> v) { return v.on(this); }
    }

    public static class Not implements Formula{
        public final Formula f;
        public Not(Formula f) { this.f = f; }
        public <R> R accept(Visitor<R> v) { return v.on(this); }
    }

    public static class And implements Formula{
        public final Formula left, right;
        public And(Formula left, Formula right) { this.left = left; this.right = right; }
        public <R> R accept(Visitor<R> v) { return v.on(this); }
    }

    public static class Or implements Formula{
        public final Formula left, right;
        public Or(Formula left, Formula right) { this.left = left; this.right = right; }
        public <R> R accept(Visitor<R> v) { return v.on(this); }        
    }
    
    // New cases
    
    public static class ForAll implements Formula{
        public final String var;
        public final Formula f;
        public ForAll(String var, Formula f) { this.var = var; this.f = f; }
        public <R> R accept(Visitor<R> v) { return v.on(this); }        
    }
    
    public static class ThereExists implements Formula{
        public final String var;
        public final Formula f;
        public ThereExists(String var, Formula f) { this.var = var; this.f = f; }
        public <R> R accept(Visitor<R> v) { return v.on(this); }        
    }
    
    // Immutable variable environments
    public static interface Env {
        public boolean lookup(String name);
        public Env set(String name, boolean value);
    }
    
    // Evaluating formulas with quantifiers
    public static boolean eval(Formula f, final Env env) {
        return f.accept(new Formula.Visitor<Boolean>() {
            public Boolean on(Var v) { return env.lookup(v.name); }
            public Boolean on(Not n) { return !eval(n.f, env); }
            public Boolean on(And a) { return eval(a.left, env) && eval(a.right, env); }
            public Boolean on(Or o) { return eval(o.left, env) || eval(o.right, env); }
            
            // Note how these cases take advantage of immutability of Env!
            public Boolean on(ForAll a) { return eval(a.f, env.set(a.var, false)) && eval(a.f, env.set(a.var, true)); }
            public Boolean on(ThereExists e) { return eval(e.f, env.set(e.var, false)) || eval(e.f, env.set(e.var, true)); }
        });
    }

}
