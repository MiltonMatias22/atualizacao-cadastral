package com.br.gsanac.repositorio;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.br.gsanac.R;
import com.br.gsanac.entidades.PavimentoRua;
import com.br.gsanac.entidades.PavimentoRua.PavimentoRuas;
import com.br.gsanac.exception.RepositorioException;
import com.br.gsanac.util.ConstantesSistema;

/**
 * @author Arthur Carvalho
 * @date 06/12/12
 */
public class RepositorioPavimentoRua extends RepositorioBase<PavimentoRua> {

    private static RepositorioPavimentoRua instance;

    public RepositorioPavimentoRua() {
        super();
    }

    public static RepositorioPavimentoRua getInstance() {
        if (instance == null) {
            instance = new RepositorioPavimentoRua();
        }
        return instance;
    }
    
    public static void removeInstance(){
        instance = null;
    }


    @Override
    public PavimentoRua pesquisar(PavimentoRua entity, String selection, String[] selectionArgs) throws RepositorioException {
        
    	Cursor cursor = null;

    	PavimentoRua pavimentoRua = new PavimentoRua();

        if (selection == null || selection.trim().equals("")) {
            selection = PavimentoRuas.ID + "=?";
        }

        if (selectionArgs == null) {
            selectionArgs = new String[] {
                String.valueOf(entity.getId())
            };
        }

        try {
            cursor = super.getDb().query(pavimentoRua.getNomeTabela(),
                                         PavimentoRua.colunas,
                                         selection,
                                         selectionArgs,
                                         null,
                                         null,
                                         null,
                                         null);

            pavimentoRua = pavimentoRua.carregarEntidade(cursor);
            
        } catch (Exception e) {
            Log.e(ConstantesSistema.LOG_TAG, e.getMessage() + " " + e.getCause());
            e.printStackTrace();
            throw new RepositorioException(context.getResources().getString(R.string.db_error));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return pavimentoRua;
    }

    @Override
    public List<PavimentoRua> pesquisarLista(String selection, String[] selectionArgs, String orderBy) throws RepositorioException {
        Cursor cursor = null;

        List<PavimentoRua> listaPavimentoRua = null;
        PavimentoRua pavimentoRua = new PavimentoRua();
        try {
        	listaPavimentoRua = new ArrayList<PavimentoRua>();

            cursor = super.getDb().query(pavimentoRua.getNomeTabela(),
                                         PavimentoRua.colunas,
                                         selection,
                                         selectionArgs,
                                         null,
                                         null,
                                         orderBy,
                                         null);
            
            
            listaPavimentoRua = pavimentoRua.carregarListaEntidade(cursor); 

        } catch (Exception e) {
            Log.e(ConstantesSistema.LOG_TAG, e.getMessage() + " " + e.getCause());
            e.printStackTrace();
            throw new RepositorioException(context.getResources().getString(R.string.db_error_search_record));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return listaPavimentoRua;
    }

    @Override
    public long inserir(PavimentoRua ps) throws RepositorioException {
        ContentValues values = new ContentValues();

       values = ps.carregarValues();
       
        try {
        	return super.getDb().insert(ps.getNomeTabela(), "", values);

        } catch (SQLException sqe) {
            Log.e(ConstantesSistema.LOG_TAG, sqe.getMessage() + " " + sqe.getCause());
            sqe.printStackTrace();
            throw new RepositorioException(context.getResources().getString(R.string.db_error_insert_record));
        }
    }

    @Override
    public void atualizar(PavimentoRua ps) throws RepositorioException {
        
    	ContentValues values =  ps.carregarValues();

        String _id = String.valueOf(ps.getId());

        String where = PavimentoRuas.ID + "=?";
        String[] whereArgs = new String[] {
            _id
        };

        try {
            super.getDb().update(ps.getNomeTabela(), values, where, whereArgs);
        } catch (SQLException sqe) {
            Log.e(ConstantesSistema.LOG_TAG, sqe.getMessage() + " " + sqe.getCause());
            sqe.printStackTrace();
            throw new RepositorioException(context.getResources().getString(R.string.db_error));
        }
    }

    @Override
    public void remover(PavimentoRua ps) throws RepositorioException {
        String _id = String.valueOf(ps.getId());

        String where = PavimentoRuas.ID + "=?";

        String[] whereArgs = new String[] {
            _id
        };

        try {
            super.getDb().delete(ps.getNomeTabela(), where, whereArgs);
        } catch (SQLException sqe) {
            Log.e(ConstantesSistema.LOG_TAG, sqe.getMessage() + " " + sqe.getCause());
            sqe.printStackTrace();
            throw new RepositorioException(context.getResources().getString(R.string.db_error));
        }
    }

}
