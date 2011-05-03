class CreateCustomers < ActiveRecord::Migration
  def self.up
    create_table :customers do |t|
      t.references :address
      t.references :gps_position
      t.string :first_name
      t.string :last_name
      t.string :phone
      
      t.timestamps
      t.timestamp :deleted_at
    end
  end

  def self.down
    drop_table :customers
  end
end
