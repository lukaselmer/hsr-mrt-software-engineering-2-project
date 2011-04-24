class CreateOrders < ActiveRecord::Migration
  def self.up
    create_table :orders do |t|
      t.datetime :created_at
      t.text :description

      t.timestamps
    end
  end

  def self.down
    drop_table :orders
  end
end
