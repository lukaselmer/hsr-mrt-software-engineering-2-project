class CreateOrders < ActiveRecord::Migration
  def self.up
    create_table :orders do |t|
      t.references :customer
      t.references :address
      t.text :description
      
      t.timestamps
    end
  end

  def self.down
    drop_table :orders
  end
end
