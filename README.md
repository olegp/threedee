You'll need javac and java installed to compile and run. On Ubuntu/Debian install it with:

    apt-get install sun-java6-jdk

Then in the project directory run:

    javac src/com/ionsquare/threedee/*.java
    java -cp src com.ionsquare.threedee.Threedee


Here's an example of how to create a scene with some lights and an object loaded from file:

    renderer.setViewport(width, height);
    renderer.setProjection(Mat4.perspective(3.1415f/2.f, 1, 20, (float)width/(float)height));

    renderer.setFrameBuffer(framebuffer);
    renderer.setRasterizer(rasterizer);

    renderer.setMaterial(new Material(new Color(0.5f, 0.5f, 0.5f),
                                      new Color(0.5f, 0.5f, 0.5f),
                                      new Color(1, 1, 1), 50));

    renderer.addLight(new Light(new Vect3(0.5f, 0.5f, -0.2f),
                      new Color(0.2f, 0.2f, 0.2f),
                      new Color(0.5f, 1, 1),
                      new Color(1, 1, 1)));


    renderer.addLight(new Light(new Vect3(1, -1, -1),
                      new Color(0, 0, 0),
                      new Color(0, 0, 1),
                      new Color(1, 1, 1)));

    visual = VisualLoader.load(getInputStream("test.3dd"));
    visual.generateNormals();
