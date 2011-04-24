class CreateAppliedMaterials < ActiveRecord::Migration
  def self.up
    create_table :applied_materials do |t|
      t.integer :amount
	  t.belongs_to :material
	  t.belongs_to :order

      t.timestamps
    end
  end

  def self.down
    drop_table :applied_materials
  end
end
