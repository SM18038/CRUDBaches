package persistencia;

import entity.Estado;
import entity.Objeto;
import entity.ObjetoEstado;
import entity.Ruta;
import entity.TipoObjeto;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.exceptions.NonexistentEntityException;

public class ControlPersistencia {
    

    //********Tabla Estado********    
    EstadoJpaController estado = new EstadoJpaController();
    String mensaje="";
    
    public void crearEStado(Estado est) {
        try {
            estado.create(est);
        } catch (Exception e) {
        }

    }

    public String eliminarEstado(int id) {
        try {
            estado.destroy(id);
        } catch (Exception e) {
            mensaje = "Nose pudo eliminar la informacion";
            System.out.println("Mensaje en eliminar: "+e.getMessage());
        }

        return mensaje;
    }

    public String modificarEStado(Estado estdo) {
        try {
            estado.edit(estdo);
        } catch (Exception ex) {
            Logger.getLogger(ControlPersistencia.class.getName()).log(Level.SEVERE, null, ex);
            mensaje = "Nose pudo actualizar la informacion";
            System.out.println("Mensaje en actualizar: "+ex.getMessage());
        }
        return mensaje;
    }

    public List<Estado> traerAlumnos() {
        List<Estado> listaEStados = estado.findEstadoEntities();

        return listaEStados;
    }

    
    
    //********Tabla Objeto********
    ObjetoJpaController obje = new ObjetoJpaController();

    public void crearObjeto(Objeto obj) {
        try {
            obje.create(obj);
        } catch (Exception e) {
        }

    }

    public String eliminarObjeto(Long id) {
        try {
            obje.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControlPersistencia.class.getName()).log(Level.SEVERE, null, ex);
            mensaje = "Nose pudo eliminar la informacion";
            System.out.println("Mensaje en eliminar: "+ex.getMessage());
        }
        return mensaje;
    }

    public void modificarObjeto(Objeto objet) {
        try {
            obje.edit(objet);
        } catch (Exception ex) {
            Logger.getLogger(ControlPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Objeto> traerObjetos() {
        List<Objeto> listaObjetos = obje.findObjetoEntities();
        return listaObjetos;
    }

    
    
    
    
    //********Tabla ObjetoEstado********
    ObjetoEstadoJpaController objeto = new ObjetoEstadoJpaController();

    public void crearObjetoEStado(ObjetoEstado obj) {
        try {
            objeto.create(obj);
        } catch (Exception e) {
        }

    }

    public String eliminarObjetoEstado(String idString) {
        try {
            objeto.destroy(Long.MIN_VALUE);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControlPersistencia.class.getName()).log(Level.SEVERE, null, ex);
            mensaje = "Nose pudo eliminar la informacion";
            System.out.println("Mensaje en eliminar: "+ex.getMessage());
        }
        
        return mensaje;
    }

    public void modificarObjetoEstado(ObjetoEstado objet) {
        try {
            objeto.edit(objet);
        } catch (Exception ex) {
            Logger.getLogger(ControlPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<ObjetoEstado> traerObjetoEstados() {
        List<ObjetoEstado> listaObjetos = objeto.findObjetoEstadoEntities();
        return listaObjetos;
    }
    
    

    //********Tabla Ruta*********
    RutaJpaController ruta = new RutaJpaController();

    public void crearRuta(Ruta rut) {
        try {
            ruta.create(rut);
        } catch (Exception e) {

        }

    }

    public String eliminarRuta(Long id) {
        try {
            ruta.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControlPersistencia.class.getName()).log(Level.SEVERE, null, ex);
            mensaje="No se pudo eliminar la informacion";
            System.out.println("Mensaje en eliminar: "+ex.getMessage());
        }
        
        return mensaje;
    }

    public void modificarRuta(Ruta rutas) {
        try {
            ruta.edit(rutas);
        } catch (Exception ex) {
            Logger.getLogger(ControlPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Ruta> traerRutas() {
        List<Ruta> listaRutas = ruta.findRutaEntities();
        return listaRutas;
    }
    
    
    
    

    //********Tabla TipoObjeto********
    TipoObjetoJpaController tbj = new TipoObjetoJpaController();

    public void crearTipoOBjeto(TipoObjeto tipo) {
        try {
            tbj.create(tipo);
        } catch (Exception e) {
        }
    }

    public String eliminartipoObjeto(String idString) {
        try {
            tbj.destroy(Integer.SIZE);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControlPersistencia.class.getName()).log(Level.SEVERE, null, ex);
            mensaje = "Nose pudo eliminar la informacion";
            System.out.println("Mensaje en eliminar: "+ex.getMessage());
        }
        return mensaje;
    }

    public void modificartipoObjeto(TipoObjeto tipo) {
        try {
            tbj.edit(tipo);
        } catch (Exception ex) {
            Logger.getLogger(ControlPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<TipoObjeto> traerTipoObjetos() {
        List<TipoObjeto> listaTipoObjetos = tbj.findTipoObjetoEntities();
        return listaTipoObjetos;
    }

}
