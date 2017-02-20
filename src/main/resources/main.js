/**
 * can access any method defined on java objects, and can also create java objects
 * using the fully-qualified class name.
 */
function greeting(name) {
	var greeting = new java.util.LinkedList();
	greeting.add('hello');
	greeting.add('world');
	greeting.add(name.toString());
	
	return greeting;
}