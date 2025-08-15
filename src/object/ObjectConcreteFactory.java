package object;



public class ObjectConcreteFactory {
	public DraggableObject createObject(String type) {
		DraggableObject object = null;
	
		if (type == "watercan")
			object = new WaterCan();
		else if (type == "seedbag")
			object = new Seedbag();
		else if (type == "umbrella")
			object = new Umbrella();
		return object;
	}
}
