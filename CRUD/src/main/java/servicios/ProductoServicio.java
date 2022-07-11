package servicios;

import modelos.*;

import jakarta.persistence.Query;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.EntityManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoServicio extends DBManager<Producto> {

    private static ProductoServicio instancia;
    private ArrayList<Producto> productos = new ArrayList<>();
    private final CarroCompra carrito = new CarroCompra();
    private ArrayList<VentasProductos> ventas = new ArrayList<>();
    private FotoServicio fotoServicio = FotoServicio.getInstancia();
    private ComentarioServicio comentarioServicio = ComentarioServicio.getInstancia();

    public static ProductoServicio getInstancia(){
        if(instancia == null){
            return new ProductoServicio();
        }
        return instancia;
    }

    private ProductoServicio(){
        super(Producto.class);
    }

    public void setProductos(Producto producto) {
        crear(producto);
    }

    public ArrayList<Producto> getProductos(){
        productos = (ArrayList<Producto>) findAll();
        return productos;
    }

    public void editarProducto(Producto producto){
        editar(producto);
    }

    public void eliminarProducto(Producto producto){
        eliminar(producto.getId());
    }

    public void eliminarFotoProducto(Producto producto, Long id) throws PersistenceException {

        for (Foto foto : producto.getFotos()) {
            if (foto.getId().equals(id)) {
                EntityManager entityManager = getEntityManager();
                entityManager.getTransaction().begin();
                Query query = entityManager.createNativeQuery("DELETE FROM PRODUCTO_FOTO WHERE PRODUCTO_ID = :idprod AND FOTOS_ID = :idfoto");
                query.setParameter("idprod", producto.getId());
                query.setParameter("idfoto", foto.getId());

                query.executeUpdate();
                entityManager.getTransaction().commit();
                entityManager.close();


                Long fotoId = foto.getId();
                producto.getFotos().remove(foto);
                fotoServicio.eliminar(fotoId);
                break;
            }
        }
    }

    public void eliminarComentarioProducto(Producto producto, Long id) throws PersistenceException {

        for (Comentario comentario : producto.getComentarios()) {
            if (comentario.getId().equals(id)) {
                EntityManager entityManager = getEntityManager();
                entityManager.getTransaction().begin();
                Query query = entityManager.createNativeQuery("DELETE FROM PRODUCTO_COMENTARIO WHERE PRODUCTO_ID = :idprod AND COMENTARIOS_ID = :idcomentario");
                query.setParameter("idprod", producto.getId());
                query.setParameter("idcomentario", comentario.getId());

                query.executeUpdate();
                entityManager.getTransaction().commit();
                entityManager.close();


                Long comentarioId = comentario.getId();
                producto.getComentarios().remove(comentario);
                comentarioServicio.eliminar(comentarioId);
                break;
            }
        }
    }

    public void insertarComentario(Producto producto, Comentario comentario){
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        Query query = entityManager.createNativeQuery("INSERT INTO PRODUCTO_COMENTARIO (PRODUCTO_ID, COMENTARIOS_ID) VALUES (:idprod, :idcom)");
        query.setParameter("idprod", producto.getId());
        query.setParameter("idcom", comentario.getId());

        query.executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public List<Producto> productosPaginados(int pag){
        int size = 10;
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNativeQuery("SELECT * FROM PRODUCTO",Producto.class);
        query.setFirstResult((pag - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    //Carrito

    public void agregarProductosCarrito(Producto producto){
        carrito.setProducto(producto);
    }

    public ArrayList<Producto> getProductosCarrito(){
        return carrito.getListaProductos();
    }

    public double getTotal(){
        double total = 0;

        for (Producto producto: carrito.getListaProductos()) {
            total += producto.getPrecio() * producto.getCantidad();
        }

        return total;
    }

    //Venta

    public ArrayList<VentasProductos> getVentas(){
        ArrayList<Producto> prods = new ArrayList<>();
        prods = carrito.getListaProductos();
        return ventas;
    }

    public void setVentas(String nombre, LocalDate fecha, ArrayList<Producto> productos){
        String query = "insert into venta(fecha, nombre_cliente) values (?,?)";
        String query2 = "select max(id) as id from venta";
        String query3 = "insert into producto_venta(id_venta, id_producto, cantidad, producto, precio) values (?,?,?,?,?)";
        Connection con = null;
        int i = 0;

        try{
            con = DatabaseServicio.getInstancia().getConexion();
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setDate(1, Date.valueOf(fecha));
            preparedStatement.setString(2, nombre);
            preparedStatement.executeUpdate();

            PreparedStatement preparedStatement2 = con.prepareStatement(query2);
            ResultSet rs = preparedStatement2.executeQuery();

            PreparedStatement preparedStatement3 = con.prepareStatement(query3);
            while (rs.next()) {
                i = rs.getInt("id");
            }

            for (Producto producto: productos) {
                preparedStatement3.setInt(1, i);
                preparedStatement3.setString(2, producto.getId());
                preparedStatement3.setInt(3, producto.getCantidad());
                preparedStatement3.setString(4, producto.getNombre());
                preparedStatement3.setDouble(5, producto.getPrecio());
                preparedStatement3.executeUpdate();
            }


        } catch (SQLException ex){
            Logger.getLogger(ProductoServicio.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductoServicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void validarProducto(String id, int cantidad){

        for (Producto producto: carrito.getListaProductos()) {
            if(producto.getId().equals(id)){
                producto.setCantidad(producto.getCantidad()+cantidad);
                return;
            }
        }

        for (Producto producto: productos) {
            if(producto.getId().equals(id)){
                producto.setCantidad(cantidad);
                agregarProductosCarrito(producto);
                return;
            }
        }

    }
}
