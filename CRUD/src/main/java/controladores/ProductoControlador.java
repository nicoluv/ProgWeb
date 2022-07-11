package controladores;

import io.javalin.Javalin;
import modelos.*;
import servicios.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ProductoControlador {
    private Javalin app;
    private ArrayList<Producto> productos;
    private ProductoServicio prodServicio = ProductoServicio.getInstancia();
    private VentasServicio ventasServicio = VentasServicio.getInstancia();
    private FotoServicio fotoServicio = FotoServicio.getInstancia();
    private ProdVentasServicio prodVentasServicio = ProdVentasServicio.getInstancia();
    private ComentarioServicio comentarioServicio = ComentarioServicio.getInstancia();

    public ProductoControlador(Javalin app) throws SQLException {
        productos = prodServicio.getProductos();
        this.app = app;
    }


    public void aplicarRutas(){
        app.get("/", ctx -> {
            ctx.redirect("comprar");
        });

        app.routes(() -> {
            path("/CRUD", () ->{
                before("/",ctx -> {
                    String usuario = ctx.cookie("usuario");
                    String sesion = ctx.cookie("sesion");

                    if(usuario == null && sesion == null){
                        ctx.redirect("/login/"+0);
                    }
                });

                get("/", ctx -> {
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("cant", prodServicio.getProductosCarrito().size());
                    modelo.put("titulo", "CRUD - Productos");
                    modelo.put("productos", productos);

                    if(ctx.cookie("usuario") != null || ctx.cookie("sesion") != null){
                        modelo.put("sesion", true);
                    }else{
                        modelo.put("sesion", false);
                    }

                    ctx.render("/publico/CRUD-Productos.html",modelo);
                });
            });
        });

        app.routes(() -> {
            path("/nuevoProducto", () ->{
                get("/", ctx -> {
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("cant", prodServicio.getProductosCarrito().size());
                    modelo.put("titulo", "Crear producto");
                    modelo.put("action", "/nuevoProducto");
                    modelo.put("boton", "Crear");

                    if(ctx.cookie("usuario") != null || ctx.cookie("sesion") != null){
                        modelo.put("sesion", true);
                    }else{
                        modelo.put("sesion", false);
                    }

                    ctx.render("/publico/crear-editar-producto.html",modelo);
                });

                post("/", ctx -> {
                    String nombre, desc;
                    double precio;
                    Set<Foto> fotos = new HashSet<>();

                    try{
                        nombre = ctx.formParam("nombre");
                        precio = Double.parseDouble(ctx.formParam("precio"));

                        desc = ctx.formParam("desc");

                        ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                            try {
                                byte[] bytes = uploadedFile.getContent().readAllBytes();
                                String encodedString = Base64.getEncoder().encodeToString(bytes);
                                Foto foto = new Foto(uploadedFile.getFilename(), uploadedFile.getContentType(), encodedString);
                                fotos.add(foto);
                                fotoServicio.setFoto(foto);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        prodServicio.setProductos(new Producto(nombre, precio, fotos, desc));
                        productos = prodServicio.getProductos();
                    }catch (Exception ex){
                        ctx.result("Error posting").status(404);
                    }
                    ctx.redirect("/CRUD");
                });

                get("/{id}", ctx -> {
                    String id = ctx.pathParam("id");

                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("cant", prodServicio.getProductosCarrito().size());
                    modelo.put("titulo", "Editar producto");
                    modelo.put("action", "/nuevoProducto/"+id);
                    modelo.put("boton", "Editar");

                    if(ctx.cookie("usuario") != null || ctx.cookie("sesion") != null){
                        modelo.put("sesion", true);
                    }else{
                        modelo.put("sesion", false);
                    }

                    for (Producto producto: productos) {
                        if(producto.getId().equals(id)){
                            modelo.put("producto", producto);
                            modelo.put("fotos", producto.getFotos());
                            break;
                        }
                    }

                    ctx.render("/publico/crear-editar-producto.html",modelo);
                });

                post("/{id}", ctx -> {
                    String id = ctx.pathParam("id"), txtDesc;

                    for (Producto producto: productos) {
                        if(producto.getId().equals(id)){
                            Set<Foto> fotos = producto.getFotos();

                            ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                                try {
                                    byte[] bytes = uploadedFile.getContent().readAllBytes();
                                    String encodedString = Base64.getEncoder().encodeToString(bytes);
                                    if(uploadedFile.getContentType().contains("image")){
                                        Foto foto = new Foto(uploadedFile.getFilename(), uploadedFile.getContentType(), encodedString);
                                        fotos.add(foto);
                                        fotoServicio.setFoto(foto);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            if(ctx.formParam("desc").equals("")){
                                txtDesc = producto.getDescripcion();
                            }else{
                                txtDesc = ctx.formParam("desc");
                            }

                            prodServicio.editarProducto(new Producto(id, ctx.formParam("nombre"),
                                                                Double.parseDouble(ctx.formParam("precio")), 0, txtDesc, fotos, producto.getComentarios()));

                            productos = prodServicio.getProductos();
                            break;
                        }
                    }

                    ctx.redirect("/CRUD");
                });

                get("/eliminar-foto/{id}/{idfoto}", ctx -> {
                    String id = ctx.pathParam("id");
                    Long idFoto = Long.parseLong(ctx.pathParam("idfoto"));
                    for (Producto producto: productos) {
                        if(producto.getId().equals(id)){
                            prodServicio.eliminarFotoProducto(producto, idFoto);
                            productos = prodServicio.getProductos();
                            break;
                        }
                    }
                    ctx.redirect("/nuevoProducto/"+id);
                });
            });
        });

        app.get("/eliminar/{id}", ctx -> {
            String id = ctx.pathParam("id");

            for (Producto producto: productos) {
                if(producto.getId().equals(id)){
                    prodServicio.eliminarProducto(producto);
                    break;
                }
            }

            productos = prodServicio.getProductos();

            ctx.redirect("/CRUD");
        });

        app.routes(() -> {
            path("/comprar", () ->{
                get("/", ctx -> {
                    int size = prodServicio.findAll().size();
                    float aux = (float) size / 5;
                    int paginasTotal = size / 5;
                    int pagina = 1;
                    int[] body;

                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("cant", prodServicio.getProductosCarrito().size());
                    modelo.put("titulo", "Comprar");

                    if(aux % 2 != 0 ){
                        paginasTotal++;
                    }

                    if(ctx.queryParam("page") == null){
                        pagina = 1;
                    }
                    else {
                        pagina = Integer.parseInt(ctx.queryParam("page")) > paginasTotal ? pagina : Integer.parseInt(ctx.queryParam("page"));
                    }

                    modelo.put("productos",prodServicio.productosPaginados(pagina));

                    if(paginasTotal > 7){
                        body = new int[paginasTotal];
                        for (int i = 0; i < paginasTotal; i++) {
                            body[i] = 1+i;
                        }
                    } else {
                        body = new int[paginasTotal];
                        for (int i = 0; i < paginasTotal; i++) {
                            body[i] = 1+i;
                        }
                    }

                    modelo.put("pagina",pagina);
                    modelo.put("total",paginasTotal);
                    modelo.put("body",body);
                    modelo.put("url","");

                    if(ctx.cookie("usuario") != null || ctx.cookie("sesion") != null){
                        modelo.put("sesion", true);
                    }else{
                        modelo.put("sesion", false);
                    }

                    ctx.render("/publico/comprar.html", modelo);
                });

                post("/{id}", ctx -> {
                    String id = ctx.pathParam("id");
                    int cantidad = Integer.parseInt(ctx.formParam("cant"));

                    prodServicio.validarProducto(id, cantidad);

                    ctx.redirect("/comprar");
                });

                get("/detalles/{id}", ctx -> {
                    String id = ctx.pathParam("id");

                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("cant", prodServicio.getProductosCarrito().size());
                    modelo.put("titulo", "Detalles del producto");
                    modelo.put("action", "/comprar/detalles/"+id);

                    if(ctx.cookie("usuario") != null || ctx.cookie("sesion") != null){
                        modelo.put("sesion", true);
                    }else{
                        modelo.put("sesion", false);
                    }

                    for (Producto producto: productos) {
                        if(producto.getId().equals(id)){
                            modelo.put("producto", producto);
                            modelo.put("fotos", producto.getFotos());
                            modelo.put("comentarios", producto.getComentarios());
                            modelo.put("cantComentarios", producto.getComentarios().size());
                            break;
                        }
                    }

                    if(productos != null){
                        modelo.put("productos", productos);
                    }

                    ctx.render("/publico/detalles.html", modelo);
                });

                post("/detalles/{id}", ctx -> {
                    String id = ctx.pathParam("id");
                    String nombre = ctx.formParam("nombre");
                    String mensaje = ctx.formParam("mensaje");

                    for (Producto producto: productos) {
                        if(producto.getId().equals(id)){
                            Comentario comentario = new Comentario(nombre, mensaje, LocalDate.now());
                            producto.getComentarios().add(comentario);
                            comentarioServicio.crear(comentario);
                            prodServicio.insertarComentario(producto, comentario);
                            productos = prodServicio.getProductos();
                            break;
                        }
                    }

                    ctx.redirect("/comprar/detalles/"+id);
                });

                get("/detalles/{id}/eliminar-comentario/{idComentario}", ctx -> {
                    String idProducto = ctx.pathParam("id");
                    Long idComentario = Long.parseLong(ctx.pathParam("idComentario"));

                    for (Producto producto: productos) {
                        if(producto.getId().equals(idProducto)){
                            prodServicio.eliminarComentarioProducto(producto, idComentario);
                            productos = prodServicio.getProductos();
                            break;
                        }
                    }

                    ctx.redirect("/comprar/detalles/"+idProducto);
                });
            });
        });

        app.get("/carrito", ctx -> {

            Map<String, Object> modelo = new HashMap<>();
            modelo.put("cant", prodServicio.getProductosCarrito().size());
            modelo.put("titulo", "Carrito de Compra");
            modelo.put("productos", prodServicio.getProductosCarrito());
            modelo.put("total", prodServicio.getTotal());

            if(ctx.cookie("usuario") != null || ctx.cookie("sesion") != null){
                modelo.put("sesion", true);
            }else{
                modelo.put("sesion", false);
            }

            ctx.render("/publico/carrito.html",modelo);
        });

        app.get("/carrito-eliminar/{id}", ctx -> {
            String id = ctx.pathParam("id");
            int i = 0;

            for (Producto producto: prodServicio.getProductosCarrito()) {
                if(producto.getId().equals(id)){
                    prodServicio.getProductosCarrito().remove(i);
                    break;
                }
                i++;
            }

            ctx.redirect("/carrito");
        });

        app.get("/carrito-limpiar", ctx -> {
            prodServicio.getProductosCarrito().removeAll(prodServicio.getProductosCarrito());
            ctx.redirect("/carrito");
        });

        app.post("/procesar", ctx -> {
            String nombreCliente = ctx.formParam("nombre");
            List<ProdVentas> listaProductos = new ArrayList<>();
            List<Producto> prods = new ArrayList<>(prodServicio.getProductosCarrito());

            for (Producto prod: prods) {
                ProdVentas prodVentas = new ProdVentas(prod.getNombre(),prod.getCantidad(),prod.getPrecio());
                listaProductos.add(prodVentas);
                prodVentasServicio.setProdVentas(prodVentas);
            }

            ventasServicio.setVentas(nombreCliente, LocalDate.now(), listaProductos);
            prodServicio.getProductosCarrito().removeAll(prodServicio.getProductosCarrito());

            ctx.redirect("/comprar");
        });

        app.routes(() -> {
            path("/ventas", () -> {
                before("/",ctx -> {
                    String usuario = ctx.cookie("usuario");
                    String sesion = ctx.cookie("sesion");

                    if(usuario == null && sesion == null){
                        ctx.redirect("/login/"+1);
                    }
                });

                get("/", ctx -> {
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("cant", prodServicio.getProductosCarrito().size());
                    modelo.put("titulo", "Lista de Ventas Realizadas");
                    modelo.put("ventas", ventasServicio.getVentas());

                    if(ctx.cookie("usuario") != null || ctx.cookie("sesion") != null){
                        modelo.put("sesion", true);
                    }else{
                        modelo.put("sesion", false);
                    }

                    ctx.render("/publico/ventas.html", modelo);
                });
            });
        });
    }

}
