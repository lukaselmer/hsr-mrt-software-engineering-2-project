class CreateCustomers < ActiveRecord::Migration
  def self.up
    create_table :customers do |t|
      t.string :first_name
      t.integer :id
      t.string :last_name
      t.string :phone
      t.integer :updated_at

      t.timestamps
    end
  end

  def self.down
    drop_table :customers
  end
end
