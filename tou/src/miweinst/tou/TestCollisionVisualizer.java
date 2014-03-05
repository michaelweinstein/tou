package miweinst.tou;

import java.awt.Color;
import java.util.Random;

import miweinst.engine.entity.DamagableEntity;
import miweinst.engine.entity.MovingEntity;
import miweinst.engine.gfx.shape.AARectShape;
import miweinst.engine.gfx.shape.CircleShape;
import miweinst.engine.gfx.shape.CompoundShape;
import miweinst.engine.gfx.shape.PolygonShape;
import miweinst.engine.gfx.shape.Shape;
import miweinst.engine.util.GameWorld;
import miweinst.engine.util.Vec2f;

/**
 * For Tou, this class is instantiated in a GameWorld, 
 * used to test collision detection on test objects.
 * Works with shapes wrapped in MovingEntity, and
 * works all in game units, in order to test
 * the system in as close a context as it will
 * really be used in the game. Creates an inner
 * subclass of MovingEntity in order to use it. So
 * this draw method is called in GameWorld, which
 * means it is inside the Graphics AffineTransform
 * and inside the Viewport's clipRect.
 * 
 * @author miweinst
 */

public class TestCollisionVisualizer {
	
	private TestEntity _testCircle, _testRect, _testCompound, _testPolygonA, _testPolygonB;
	private TestEntity[] _testArr;
	
	private Vec2f _mouseLast;
	private int _currIndex;

	public TestCollisionVisualizer(GameWorld world) {

		_testCircle = new TestEntity(world, "circle");
//		_testSquare = new TestEntity(world, "square");
		_testRect = new TestEntity(world, "rect");
		_testCompound = new TestEntity(world, "compound");
		_testPolygonA = new TestEntity(world, "polygonA");
		_testPolygonB = new TestEntity(world, "polygonB");
		
		_testArr = new TestEntity[5];
		_testArr[0] = _testCircle; 
//		_testArr[1] = _testSquare; 
		_testArr[1] = _testRect;
		_testArr[2] = _testCompound;
		_testArr[3] = _testPolygonA;
		_testArr[4] = _testPolygonB;
	}
	
	//Returns all shapes
	public TestEntity[] toArr() {
		return _testArr;
	}
	
	public void onTick(long nanos) {
//Random colors on tick for Collision
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		
		//Collision Check!
		for (int i=0; i<_testArr.length; i++) {
			for (int j=0; j<_testArr.length; j++) {
				if (_testArr[i] != _testArr[j]) {
					if (_testArr[i].collides(_testArr[j]) && _testArr[i] != _testArr[j]) {
						_testArr[i].setShapeColor(new Color(r,g,b));
						_testArr[j].setShapeColor(new Color(r,g,b));
					}
				}
			}
		}
	}
	
	/*Takes in location of where the mouse goes down, used
	 * to measure the change in mouse location when 
	 * MouseDragged finds next loc.*/
	public void onMousePressed(Vec2f e) {
		MovingEntity[] arr = this.toArr();
		boolean inShape = false;
		for (int i=0; i<arr.length; i++) {
			if (arr[i].contains(e)) {
				inShape = true;
				_currIndex = i;
			}
		}
		if (inShape == false) _currIndex = arr.length+1;
		_mouseLast = e;
	}
	/*Takes in the new mouse location in game units, and the 
	 * current scale.*/
	public void onMouseDragged(Vec2f e, float scale) {
		MovingEntity[] arr =this.toArr();
		float dx = e.x - _mouseLast.x;
		float dy = e.y -  _mouseLast.y;
		if (_currIndex < arr.length) 
			arr[_currIndex].move(dx, dy);	
		_mouseLast = e;
	}

/**This is an inner class in order to use Visualizer with
 * Entities that wrap the shapes. It is a test Entity to
 * test shapes and collisions within. Simple conditionals
 * in order to add new shapes, etc...*/
private class TestEntity extends DamagableEntity {

	private Shape test_shape;
	
	public TestEntity(GameWorld world, String shape) {
		super(world);
		
		if (shape == "circle") {
			Vec2f circloc = new Vec2f(26, 18);
			float radius = 5;
			CircleShape circle = new CircleShape(circloc, radius);
			circle.setColor(Color.PINK);
			circle.setBorderColor(Color.WHITE);
			circle.setBorderWidth(0);
			test_shape = circle;
		} else if (shape == "square") {
			Vec2f squareLoc = new Vec2f(13, 13);
			Vec2f squareDim = new Vec2f(5, 5);
			AARectShape square = new AARectShape(squareLoc, squareDim);
			square.setColor(Color.ORANGE);
			square.setBorderColor(Color.WHITE);
			square.setBorderWidth(0);
//			test_shape = square;
		} else if (shape == "rect") {
			Vec2f rectloc  = new Vec2f(34, 8);
			Vec2f rectdim = new Vec2f(7, 4);
			AARectShape rect = new AARectShape(rectloc, rectdim);
			rect.setColor(Color.GREEN);
			rect.setBorderColor(Color.WHITE);
			rect.setBorderWidth(0);
			test_shape = rect;
		} else if (shape == "compound") {
			Shape[] shapes = new Shape[3];
			shapes[0] = new CircleShape(new Vec2f(10, 10), 4);
			shapes[1] = new CircleShape(new Vec2f(3, 10), 6);
			shapes[2] = new AARectShape(new Vec2f(15, 9), new Vec2f(7, 2));
			CompoundShape compound = new CompoundShape(Color.CYAN, new Vec2f(10, 10), shapes);
			test_shape = compound;
		} else if (shape == "polygonA") {
			Vec2f[] verts = new Vec2f[3];
			//Counter-Clockwise order
			verts[0] = new Vec2f(15, 16);
			verts[1] = new Vec2f(11, 11);
			verts[2] = new Vec2f(11, 21);
			PolygonShape poly = new PolygonShape(new Vec2f(11, 11), verts);
			poly.setOutline(Color.RED, .14f);
			test_shape = poly;
		} else if (shape == "polygonB") {
			Vec2f[] verts = new Vec2f[3];
			//Counter-Clockwise order
			verts[0] = new Vec2f(28, 14);
			verts[1] = new Vec2f(24, 14);
			verts[2] = new Vec2f(26, 18);			
			PolygonShape poly = new PolygonShape(new Vec2f(24, 4), verts);
			poly.setOutline(Color.MAGENTA, .14f);
			test_shape = poly;
		}
		super.setShape(test_shape);
	}	
}
}
