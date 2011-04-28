class CreateAppliedMaterials < ActiveRecord::Migration
  def self.up
    create_table :applied_materials do |t|
      t.references :material
      t.references :order
      t.integer :amount
      
      t.timestamps
    end
  end

  def self.down
    drop_table :applied_materials
  end
end
