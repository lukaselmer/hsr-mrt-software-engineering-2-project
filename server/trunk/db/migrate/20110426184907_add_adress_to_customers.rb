class AddAdressToCustomers < ActiveRecord::Migration
  def self.up
    add_column :customers, :address_id, :integer
	add_column :customers, :location_id, :integer
  end

  def self.down
    remove_column :customers, :adress_id
	remove_column :customers, :location_id
  end
end
