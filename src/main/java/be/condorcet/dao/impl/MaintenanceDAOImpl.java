package be.condorcet.dao.impl;

import be.condorcet.dao.MaintenanceDAO;
import be.condorcet.model.Maintenance;

public class MaintenanceDAOImpl extends GenericDAOImpl<Maintenance, Long> implements MaintenanceDAO {

    public MaintenanceDAOImpl() {
        super(Maintenance.class);
    }
}
